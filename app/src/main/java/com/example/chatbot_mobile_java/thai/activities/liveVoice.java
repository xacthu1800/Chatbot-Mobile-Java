package com.example.chatbot_mobile_java.thai.activities;

import com.example.chatbot_mobile_java.R;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.*;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.hardware.camera2.CameraManager;
import com.google.android.material.button.MaterialButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class liveVoice extends AppCompatActivity {

    private TextureView textureView;
    private MaterialButton captureButton;
    private Button startButton;
    private Button stopButton;
    private TextView chatLog;
    private ImageView statusIndicator;
    private String currentFrameB64;
    private WebSocketClient webSocket;
    private boolean isRecording = false;
    private AudioRecord audioRecord;
    private List<Short> pcmData = new ArrayList<>();
    private Thread recordThread;
    private final String MODEL = "models/gemini-2.0-flash-exp";
    private final String API_KEY = "AIzaSyBIeyvhotVUZCo-pQikb1noVvk8OC5_YgM"; // Replace with your actual API key
    private final String HOST = "generativelanguage.googleapis.com";
    private final String URL = "wss://" + HOST + "/ws/google.ai.generativelanguage.v1alpha.GenerativeService.BidiGenerateContent?key=" + API_KEY;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int AUDIO_REQUEST_CODE = 200;
    private static final int AUDIO_SAMPLE_RATE = 24000;
    private static final int RECEIVE_SAMPLE_RATE = 24000;
    private static final int AUDIO_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private static final int AUDIO_BUFFER_SIZE = AudioRecord.getMinBufferSize(AUDIO_SAMPLE_RATE, AUDIO_CHANNEL_CONFIG, AUDIO_ENCODING);

    private final List<byte[]> audioQueue = new ArrayList<>();
    private boolean isPlaying = false;
    private AudioTrack audioTrack;

    private static final int MAX_IMAGE_DIMENSION = 1024;
    private static final int JPEG_QUALITY = 70;
    private long lastImageSendTime = 0;
    private static final long IMAGE_SEND_INTERVAL = 3000;
    private boolean isConnected = false;
    private boolean isSpeaking = false;

    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;
    private CaptureRequest.Builder captureRequestBuilder;
    private ImageReader imageReader;
    private final HandlerThread cameraThread = new HandlerThread("CameraThread");
    private Handler cameraHandler;
    private String cameraId;
    private Size previewSize;

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
    private boolean isCameraActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_voice);

        textureView = findViewById(R.id.textureView);
        captureButton = findViewById(R.id.captureButton);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        chatLog = findViewById(R.id.chatLog);
        statusIndicator = findViewById(R.id.statusIndicator);
        updateStatusIndicator();

        cameraThread.start();
        cameraHandler = new Handler(cameraThread.getLooper());

        captureButton.setIconResource(R.drawable.camera_on);

        captureButton.setOnClickListener(v -> {
            if (isCameraActive) {
                stopCameraPreview();
                captureButton.setIconResource(R.drawable.camera_on);
                isCameraActive = false;
            } else {
                if (textureView.isAvailable()) {
                    startCameraPreview();
                    captureButton.setIconResource(R.drawable.camera_off);
                    isCameraActive = true;
                } else {
                    textureView.setSurfaceTextureListener(surfaceTextureListener);
                }
            }
        });

        startButton.setOnClickListener(v -> checkRecordAudioPermission());
        stopButton.setOnClickListener(v -> stopAudioInput());

        connect();
    }

    private final TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            startCameraPreview();
            captureButton.setIconResource(R.drawable.camera_off);
            isCameraActive = true;
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {}

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            stopCameraPreview();
            captureButton.setIconResource(R.drawable.camera_off);
            isCameraActive = false;
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {}
    };

    private void startCameraPreview() {
        checkCameraPermissionForPreview();
    }

    private void stopCameraPreview() {
        closeCamera();
        SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
        if (surfaceTexture != null) {
            Surface surface = new Surface(surfaceTexture);
            android.graphics.Canvas canvas = surface.lockCanvas(null);
            if (canvas != null) {
                canvas.drawColor(android.graphics.Color.BLACK);
                surface.unlockCanvasAndPost(canvas);
            }
            surface.release();
        }
    }

    private void checkCameraPermissionForPreview() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            openCameraForPreview();
        }
    }

    private void openCameraForPreview() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            if (map == null) return;
            previewSize = map.getOutputSizes(SurfaceTexture.class)[0];

            imageReader = ImageReader.newInstance(MAX_IMAGE_DIMENSION, MAX_IMAGE_DIMENSION, ImageFormat.JPEG, 2);
            imageReader.setOnImageAvailableListener(imageAvailableListener, cameraHandler);

            cameraManager.openCamera(cameraId, cameraStateCallback, cameraHandler);
        } catch (CameraAccessException e) {
            Log.e("Camera", "Error opening camera", e);
        } catch (SecurityException e) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    private final CameraDevice.StateCallback cameraStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            cameraDevice = camera;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
            cameraDevice = null;
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
            Log.e("Camera", "Camera error: " + error);
        }
    };

    private void createCameraPreviewSession() {
        try {
            SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
            if (surfaceTexture != null) {
                surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
            }
            Surface previewSurface = new Surface(surfaceTexture);

            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(previewSurface);
            captureRequestBuilder.addTarget(imageReader.getSurface());

            cameraDevice.createCaptureSession(
                    List.of(previewSurface, imageReader.getSurface()),
                    cameraCaptureSessionCallback,
                    cameraHandler
            );
        } catch (CameraAccessException e) {
            Log.e("Camera", "Error creating preview session", e);
        }
    }

    private final CameraCaptureSession.StateCallback cameraCaptureSessionCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(CameraCaptureSession session) {
            cameraCaptureSession = session;
            updatePreview();
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {
            Toast.makeText(liveVoice.this, "Configuration failed", Toast.LENGTH_SHORT).show();
        }
    };

    private void updatePreview() {
        if (cameraDevice == null) return;

        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
        HandlerThread thread = new HandlerThread("UpdatePreview");
        thread.start();
        Handler handler = new Handler(thread.getLooper());
        try {
            cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, handler);
        } catch (CameraAccessException e) {
            Log.e("Camera", "Error starting preview repeat request", e);
        }
    }

    private void closeCamera() {
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (imageReader != null) {
            imageReader.close();
            imageReader = null;
        }
    }

    private final ImageReader.OnImageAvailableListener imageAvailableListener = reader -> {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastImageSendTime >= IMAGE_SEND_INTERVAL) {
            Image image = reader.acquireLatestImage();
            if (image == null) return;
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            image.close();

            new Thread(() -> processAndSendImage(bytes)).start();
            lastImageSendTime = currentTime;
            Log.d("ImageCapture", "Image processed and sent based on time interval");
        } else {
            Image image = reader.acquireLatestImage();
            if (image != null) image.close();
            Log.d("ImageCapture", "Image capture skipped: Not enough time elapsed");
        }
    };

    private void processAndSendImage(byte[] imageBytes) {
        String currentTime = timeFormat.format(new Date());
        Log.d("ImageCapture", "Image processed and sending at: " + currentTime);

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        Bitmap scaledBitmap = scaleBitmap(bitmap, MAX_IMAGE_DIMENSION);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, byteArrayOutputStream);

        String b64Image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT | Base64.NO_WRAP);
        sendMediaChunk(b64Image, "image/jpeg");

        scaledBitmap.recycle();
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            Log.e("ImageCapture", "Error closing stream", e);
        }
    }

    private Bitmap scaleBitmap(Bitmap bitmap, int maxDimension) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width <= maxDimension && height <= maxDimension) {
            return bitmap;
        }

        int newWidth, newHeight;
        if (width > height) {
            float ratio = (float) width / maxDimension;
            newWidth = maxDimension;
            newHeight = Math.round(height / ratio);
        } else {
            float ratio = (float) height / maxDimension;
            newHeight = maxDimension;
            newWidth = Math.round(width / ratio);
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCameraPreview();
                } else {
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            case AUDIO_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startAudioInput();
                } else {
                    Toast.makeText(this, "Audio permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void checkRecordAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_REQUEST_CODE);
        } else {
            startAudioInput();
        }
    }

    private void connect() {
        Log.d("WebSocket", "Connecting to: " + URL);
        java.util.Map<String, String> headers = new java.util.HashMap<>();
        headers.put("Content-Type", "application/json");

        try {
            webSocket = new WebSocketClient(new URI(URL), new Draft_6455(), headers) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d("WebSocket", "Connected. Server handshake: " + handshakedata.getHttpStatus());
                    isConnected = true;
                    updateStatusIndicator();
                    sendInitialSetupMessage();
                }

                @Override
                public void onMessage(String message) {
                    Log.d("WebSocket", "Message Received: " + message);
                    receiveMessage(message);
                }

                @Override
                public void onMessage(ByteBuffer bytes) {
                    if (bytes != null) {
                        String message = new String(bytes.array(), java.nio.charset.StandardCharsets.UTF_8);
                        receiveMessage(message);
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("WebSocket", "Connection Closed: " + reason);
                    isConnected = false;
                    updateStatusIndicator();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(liveVoice.this, "Connection closed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(Exception ex) {
                    Log.e("WebSocket", "Error: " + ex.getMessage());
                    isConnected = false;
                    updateStatusIndicator();
                }
            };
            webSocket.connect();
        } catch (URISyntaxException e) {
            Log.e("WebSocket", "Invalid URI: " + URL, e);
            // Optionally, show a toast or handle the error further
        }
    }

    private void sendInitialSetupMessage() {
        Log.d("WebSocket", "Sending initial setup message");
        JSONObject setupMessage = new JSONObject();
        JSONObject setup = new JSONObject();
        JSONObject generationConfig = new JSONObject();
        JSONArray responseModalities = new JSONArray();
        responseModalities.put("AUDIO");
        try {
            generationConfig.put("response_modalities", responseModalities);
            setup.put("model", MODEL);
            setup.put("generation_config", generationConfig);
            setupMessage.put("setup", setup);
        } catch (org.json.JSONException e) {
            Log.e("WebSocket", "JSON error", e);
        }
        Log.d("WebSocket", "Sending config payload: " + setupMessage);
        webSocket.send(setupMessage.toString());
    }

    private void sendMediaChunk(String b64Data, String mimeType) {
        if (!isConnected) {
            Log.d("WebSocket", "WebSocket not connected");
            return;
        }

        JSONObject msg = new JSONObject();
        JSONObject realtimeInput = new JSONObject();
        JSONArray mediaChunks = new JSONArray();
        JSONObject chunk = new JSONObject();
        try {
            chunk.put("mime_type", mimeType);
            chunk.put("data", b64Data);
            mediaChunks.put(chunk);
            realtimeInput.put("media_chunks", mediaChunks);
            msg.put("realtime_input", realtimeInput);
        } catch (org.json.JSONException e) {
            Log.e("WebSocket", "JSON error", e);
        }

        webSocket.send(msg.toString());
    }

    private void receiveMessage(String message) {
        if (message == null) return;

        try {
            JSONObject messageData = new JSONObject(message);
            if (messageData.has("serverContent")) {
                JSONObject serverContent = messageData.getJSONObject("serverContent");
                if (serverContent.has("modelTurn")) {
                    JSONObject modelTurn = serverContent.getJSONObject("modelTurn");
                    if (modelTurn.has("parts")) {
                        JSONArray parts = modelTurn.getJSONArray("parts");
                        for (int i = 0; i < parts.length(); i++) {
                            JSONObject part = parts.getJSONObject(i);
                            if (part.has("text")) {
                                String text = part.getString("text");
                                displayMessage("GEMINI: " + text);
                            }
                            if (part.has("inlineData")) {
                                JSONObject inlineData = part.getJSONObject("inlineData");
                                if (inlineData.has("mimeType") && "audio/pcm;rate=24000".equals(inlineData.getString("mimeType"))) {
                                    String audioData = inlineData.getString("data");
                                    injestAudioChunkToPlay(audioData);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Receive", "Error parsing message", e);
        }
    }

    private byte[] base64ToArrayBuffer(String base64) {
        return Base64.decode(base64, Base64.DEFAULT);
    }

    private float[] convertPCM16LEToFloat32(byte[] pcmData) {
        short[] shortArray = asShortArray(pcmData);
        float[] floatArray = new float[shortArray.length];
        for (int i = 0; i < shortArray.length; i++) {
            floatArray[i] = shortArray[i] / 32768f;
        }
        return floatArray;
    }

    private short[] asShortArray(byte[] byteArray) {
        short[] shortArray = new short[byteArray.length / 2];
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < shortArray.length; i++) {
            shortArray[i] = byteBuffer.getShort();
        }
        return shortArray;
    }

    private void injestAudioChunkToPlay(String base64AudioChunk) {
        if (base64AudioChunk == null) return;

        new Thread(() -> {
            try {
                byte[] arrayBuffer = base64ToArrayBuffer(base64AudioChunk);
                synchronized (audioQueue) {
                    audioQueue.add(arrayBuffer);
                }
                if (!isPlaying) {
                    playNextAudioChunk();
                }
                Log.d("AudioChunk", "Audio chunk added to the queue");
            } catch (Exception e) {
                Log.e("AudioChunk", "Error processing chunk", e);
            }
        }).start();
    }

    private void playNextAudioChunk() {
        new Thread(() -> {
            while (true) {
                byte[] chunk;
                synchronized (audioQueue) {
                    if (audioQueue.isEmpty()) break;
                    chunk = audioQueue.remove(0);
                }
                isPlaying = true;
                playAudio(chunk);
            }
            isPlaying = false;

            synchronized (audioQueue) {
                if (!audioQueue.isEmpty()) {
                    playNextAudioChunk();
                }
            }
        }).start();
    }

    private void playAudio(byte[] byteArray) {
        if (audioTrack == null) {
            audioTrack = new AudioTrack(
                    android.media.AudioManager.STREAM_MUSIC,
                    RECEIVE_SAMPLE_RATE,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    AudioTrack.getMinBufferSize(RECEIVE_SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT),
                    AudioTrack.MODE_STREAM
            );
        }

        audioTrack.write(byteArray, 0, byteArray.length);
        audioTrack.play();
        new Thread(() -> {
            while (audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Log.e("Audio", "Sleep interrupted", e);
                }
            }
            audioTrack.stop();
        }).start();
    }

    private void startAudioInput() {
        if (isRecording) return;
        isRecording = true;
        audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.VOICE_COMMUNICATION,
                AUDIO_SAMPLE_RATE,
                AUDIO_CHANNEL_CONFIG,
                AUDIO_ENCODING,
                AUDIO_BUFFER_SIZE
        );

        if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
            Log.e("Audio", "AudioRecord initialization failed");
            return;
        }

        audioRecord.startRecording();
        Log.d("Audio", "Start Recording");
        isSpeaking = true;
        updateStatusIndicator();

        recordThread = new Thread(() -> {
            while (isRecording) {
                short[] buffer = new short[AUDIO_BUFFER_SIZE];
                int readSize = audioRecord.read(buffer, 0, buffer.length);
                if (readSize > 0) {
                    for (int i = 0; i < readSize; i++) {
                        pcmData.add(buffer[i]);
                    }
                    if (pcmData.size() >= readSize) {
                        recordChunk();
                    }
                }
            }
        });
        recordThread.start();
    }

    private void recordChunk() {
        if (pcmData.isEmpty()) return;

        new Thread(() -> {
            ByteBuffer buffer = ByteBuffer.allocate(pcmData.size() * 2).order(ByteOrder.LITTLE_ENDIAN);
            for (Short value : pcmData) {
                buffer.putShort(value);
            }
            byte[] byteArray = buffer.array();
            String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT | Base64.NO_WRAP);
            Log.d("Audio", "Send Audio Chunk");
            sendMediaChunk(base64, "audio/pcm");

            pcmData.clear();
        }).start();
    }

    private void stopAudioInput() {
        isRecording = false;
        if (recordThread != null) {
            try {
                recordThread.join();
            } catch (InterruptedException e) {
                Log.e("Audio", "Thread join interrupted", e);
            }
        }
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
        Log.d("Audio", "Stop Recording");
        isSpeaking = false;

        // 2. Stop playback and clear audio queue
        synchronized (audioQueue) {
            audioQueue.clear();  // Clear any queued audio chunks
        }
        if (audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();
            audioTrack = null;
            isPlaying = false;
        }
        Log.d("Audio", "Playback stopped");

        // 3. Cut off WebSocket and reset the model
        if (webSocket != null) {
            if (webSocket.isOpen()) {
                webSocket.close();
                Log.d("WebSocket", "WebSocket connection terminated");
            }
            webSocket = null;  // Clear reference
        }
        isConnected = false;

        // 4. Reinitialize WebSocket to reset model state
        connect();  // Reconnect to reset the model to initial state
        Log.d("WebSocket", "Reconnecting to reset model state");

        updateStatusIndicator();
    }

    private void displayMessage(String message) {
        Log.d("Chat", "Displaying message: " + message);
        runOnUiThread(() -> {
            String currentText = chatLog.getText().toString();
            chatLog.setText(currentText + "\n" + message);
        });
    }

    private void updateStatusIndicator() {
        runOnUiThread(() -> {
            if (!isConnected) {
                statusIndicator.setImageResource(R.drawable.baseline_error_24);
                statusIndicator.setColorFilter(android.graphics.Color.RED);
            } else if (!isSpeaking) {
                statusIndicator.setImageResource(R.drawable.baseline_equalizer_24);
                statusIndicator.setColorFilter(android.graphics.Color.GRAY);
            } else {
                statusIndicator.setImageResource(R.drawable.baseline_equalizer_24);
                statusIndicator.setColorFilter(android.graphics.Color.GREEN);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraThread.quitSafely();
        closeCamera();
    }
}
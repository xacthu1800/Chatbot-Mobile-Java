package com.example.chatbot_mobile_java.bin.data;

public class clientMessage {
    public static String modelType;
    public static String clientMessText;

    static void  initialize_Type_Text (
            String type_model,
            String text_client
    ){
        modelType = type_model;
        clientMessText = text_client ;
    }

    public static void initialize_Type(String type_model){
        modelType = type_model;
    }

    public static void initialize_Text ( String text_client){
        clientMessText = text_client ;
    }

    public static String get_Type(){
        if(modelType == null){
            return "0";
        }
        return modelType;
    }

    public static String get_Text(){
        if(clientMessText == null){
            return "0";
        }
        return clientMessText;
    }

}

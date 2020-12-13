package com.marquelo.getstars.working;

import java.util.Random;

public class CodigoRandom {
    public CodigoRandom(){

    }

    public String generarCodigoRandom(int size){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123465790".toCharArray();
        StringBuilder sb = new StringBuilder(size);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }
}
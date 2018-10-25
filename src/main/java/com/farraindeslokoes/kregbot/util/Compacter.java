package com.farraindeslokoes.kregbot.util;

public class Compacter {    //comentario do bignelli : eu do split antes, e isso aqui inutiliza aquilo. faco a mesma coisa em outras coisas.
    public static String compactString(String[] strings) { //talvez seja melhor ajustar a classe commands/commandevent para
        StringBuilder newString = new StringBuilder();     //mandar splittado quando util e nao quando for inutil?
        for (String str: strings) {
            newString.append(str);
        }
        return newString.toString();
    }
}

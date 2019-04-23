package net.bonsamigos.util;

public class NomeComInicialMaiscula {
	
	/**
     * Pega qualquer String e coloca as iniciais maiuscula
     * @return
     */
    public static String iniciaisMaiuscula(String nome) {

        String sNova = "";
        for (String sNome : nome.toLowerCase().split(" ")) {
            if (!"".equals(sNome)) {
                if (!"".equals(sNova))
                    sNova += " ";
                if (sNome.length() > 2) {
                    sNova += sNome.substring(0, 1).toUpperCase() + sNome.substring(1);
                } else {
                    sNova += sNome;
                }
            }
        }
        return sNova;
    }

}

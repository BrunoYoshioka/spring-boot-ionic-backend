package com.bruno.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {
	
	public static String decodeParam(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	public static List<Integer> decodeIntList(String s){
		String[] vet = s.split(","); // criar um vetor de string que separa string por vírgula
		List<Integer> list = new ArrayList<>(); // instaciar a lista
		// para cada posição do meu vetor
		for(int i=0;i<vet.length;i++) {
			list.add(Integer.parseInt(vet[i])); // converter o elemento da posição i do meu vetor para inteiro
		}
		return list;
		// return Arrays.asList(s.split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
	}
}

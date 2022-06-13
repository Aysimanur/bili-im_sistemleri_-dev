import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class sifrelemeOdevi {

	// t�rk�e kelimeleri tuttu�umuz listemiz
	static ArrayList<String> trCharcer = new ArrayList<String>();
	// sezar �ifre k�rma methotomuz i�in es ge�ilecek karakterler
	static String esGec = "������������.,'��!? ";

	static HashMap<Integer, Character> numberToCharacter = new HashMap<Integer, Character>();
	static HashMap<Character, Integer> characterToNumber = new HashMap<Character, Integer>();
	// harfleri string olarak tan�ml�yoruz text inizde bulunacak olan karakterleri alta ekleyebilirsiniz
	static String Letter = "0123456789.,abc�defg�h�ijklmno�prs�tu�vyzABC�DEFG�HI�JKLMNO�PRS�TU�VYZ";

	public static void main(String[] args) {
		// txt belgelerindeki t�rk�e kelimeleri liste haline getirip trCharcer listemize
		// at�yoruz.
		File file = new File("src\\\\turkce\\\\TDK_Kelime_Listesi.txt");

		try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"))) {
			String str;

			while ((str = in.readLine()) != null) {
				// kelimeleri ekliyoruz
				trCharcer.add(str);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// �ifreleme yapmak i�in alfabeyi elle ekledim
		for (int i = 0; i < Letter.length(); i++) {
			// say�dan harf �a��rmak i�in
			numberToCharacter.put(i, Letter.charAt(i));
			// harfden say� �a��rmak i�in
			characterToNumber.put(Letter.charAt(i), i);
		}

		

		// offset vererek metnimizi  �ifreliyoruz
		String sezarSifreli = caesarEncryption("sima", 7);
		System.out.println("�ifreli metin: " + sezarSifreli);
		// sezarla �ifrelenmi� metini k�rmay� denemekteyiz
		String sezarKir = decodeText(sezarSifreli);
		System.out.println(sezarKir);

		// bu k�s�mda text i Ybs key i ile �ifreliyoruz
		String s = encryptWithKey("�al���yoruz.", "Ybs");
		// ��kan �ifrelenmi� metin
		System.out.println("�ifreli metin :" + s);
		// ayn� key ile �ifreyi k�r�yoruz
		System.out.println(decryptWith(s, "Ybs"));
	}
	public static String decodeText(String sifreli) {
		String enteredPassword = "";
		String[] encryptedList = sifreli.split(" ");
		int offset = 0;
	
		// 26 seferlik bir loop ald�k
		while (offset < 27) {
			// c�mlenin ilk kelimesini k�rmay� deniyoruz sonu� al�rsak �ifre ��z�lm��
			// oluyor
			String s1 = caesarDecrypt(encryptedList[0], offset);
			if (trCharcer.contains(s1.toLowerCase())) {
				enteredPassword += s1 + " ";

			} else {
				enteredPassword = "";
				offset += 1;

			}

			// e�er k�r�lan �ifre degi�keni bo� degilse bir kelime bulunmu� demektir bu
			// a�amada tahmini ofsetimiz ��km�� oluyor
			if (enteredPassword != "") {
				// buldu�umuz ofset ile t�m metni k�r�yoruz
				enteredPassword = caesarDecrypt(sifreli, offset);
				// art�k d�dg�yle i�imiz kalmad��� i�in k�r�yoruz d�ng�m�z�
				break;
			}
		}

		return "Text : " + enteredPassword + " Offset :" + offset;
	}

	// assci tablosuna g�re sezar �ifreleme methotumuz
	//Burada klasik bir sezar �ifreleme yapt���m�z i�in a��klama gerek duymad�m.

	public static String caesarEncryption(String text, int offsetMod) {
		try

		{
			// �telenecek offsetin alfabeyi ta�mamas� i�in modunu al�yoruz
			int offset = offsetMod % 26;
			char[] letters = text.toCharArray();

			String encrypted = "";

			for (int i = 0; i < letters.length; i++) {
				if (esGec.contains(Character.toString(letters[i]))) {
					encrypted += Character.toString(letters[i]);
				} else {
					int newOffset = (letters[i] + offset);
					if (96 < newOffset) {
						if (newOffset > 122) {
							int lastOffset = newOffset - 122;
							encrypted += Character.toString((char) (96 + lastOffset));
						} else {
							encrypted += Character.toString((char) (newOffset));
						}
					} else {
						if (newOffset > 90) {
							int lastOffset = newOffset - 90;
							encrypted += Character.toString((char) (64 + lastOffset));
						} else {
							encrypted += Character.toString((char) (newOffset));
						}
					}
				}

			}

			return encrypted;

		}

		catch (Exception ex)

		{
			System.out.println(ex.getMessage());

			return null;

		}

	}

	// assci tablosuna g�re �ifrelenmi� sezar �ifresi k�rma methotumuz
	// �ifreleme methotundan tek fark� - ��kartma i�lemi olan k�s�mlard�r
	public static String caesarDecrypt(String metin, int ooffsetMod) {
		try

		{
			int offset = ooffsetMod % 26;

			char[] letters = metin.toCharArray();

			String encrypted = "";

			for (int i = 0; i < letters.length; i++) {
				if (esGec.contains(Character.toString(letters[i]))) {
					encrypted += Character.toString(letters[i]);
				} else {
					int offsetIndex = (letters[i] - offset);
					if (64 < letters[i] && 91 > letters[i]) {
						if (offsetIndex < 65) {
							int lastoffsetIndex = 65 - offsetIndex;

							encrypted += Character.toString((char) (91 - lastoffsetIndex));
						} else {
							encrypted += Character.toString((char) (offsetIndex));
						}
					} else {
						if (offsetIndex < 97) {
							int lastoffsetIndex = 97 - offsetIndex;
							encrypted += Character.toString((char) (123 - lastoffsetIndex));
						} else {
							encrypted += Character.toString((char) (offsetIndex));
						}
					}
				}

			}

			return encrypted;

		}

		catch (Exception ex)

		{
			System.out.println(ex.getMessage());

			return null;

		}

	}


	// bu methot 2. k�s�m i�indir
	private static String encryptWithKey(String text, String key) {
		String ciphertext = "";
		int offset = 0;
		// gelen key  stringin her bir karakterini listeye at�yoruz
		char[] keyToCharList = key.toCharArray();
		// gelen text stringinin her bir karakterini  listeye at�yoruz
		char[] textToCharList = text.toCharArray();

		// text in her bir karakterini �ifrelemek i�in for loop a sokuyoruz
		for (int i = 0; i < textToCharList.length; i++) {
			// key ile text in o indisindeki karakterlerin numberOfCharacters map indeki kar��l�k
			// gelen rakamlar� birbirinden ��kart�yoruz
			offset = characterToNumber.get(textToCharList[i]) + characterToNumber.get(keyToCharList[i % keyToCharList.length]);

			if (offset != 0)
				ciphertext += numberToCharacter.get(offset % numberToCharacter.size());
			else
				ciphertext += numberToCharacter.get(offset);
		}
		// �ifreledigimiz metni geri return ediyoruz
		return ciphertext;

	}

	// bu methot 2. k�s�m i�indir
	private static String decryptWith(String text, String key) {
		String encrypted = "";
		int offset = 0;
		// gelen key in her bir karakterini asci kodunu listeye att�m
		char[] keyToCharList = key.toCharArray();
		// gelen text in her bir karakterini asci kodunu listeye att�m
		char[] textToCharList = text.toCharArray();

		// text in her bir karakterini �ifrelemek i�in for loop a sokuyoruz
		for (int i = 0; i < textToCharList.length; i++) {
			// key ile text in o indisindeki karakterlerin asciAlfabe map indeki kar��l�k
			// gelen rakamlar� birbirinden ��kart�yoruz
			offset = characterToNumber.get(textToCharList[i]) - characterToNumber.get(keyToCharList[i % keyToCharList.length]);

			if (offset != 0) {
			if (offset > 0)
				encrypted += numberToCharacter.get(offset % numberToCharacter.size());
			else // ofset "-" de�eri al�rsa alfabe boyutu ile  toplama i�lemi yapt�r�yoruz. Bu sayede sondan geriye gelmi� oluyrouz.
				encrypted += numberToCharacter.get(numberToCharacter.size()+(offset % numberToCharacter.size()));
			}else// 0 �n mod unu alm�yoruz. ��nk� 0 hi�bir say�ya b�l�nmemez.
				encrypted += numberToCharacter.get(offset);
		}
		// �ifreledigimiz metini return ediyoruz.
		return encrypted;

	}

	
}

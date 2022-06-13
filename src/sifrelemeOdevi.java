import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class sifrelemeOdevi {

	// türkçe kelimeleri tuttuðumuz listemiz
	static ArrayList<String> trCharcer = new ArrayList<String>();
	// sezar þifre kýrma methotomuz için es geçilecek karakterler
	static String esGec = "ðÐçÇþÞüÜöÖýÝ.,'“”!? ";

	static HashMap<Integer, Character> numberToCharacter = new HashMap<Integer, Character>();
	static HashMap<Character, Integer> characterToNumber = new HashMap<Character, Integer>();
	// harfleri string olarak tanýmlýyoruz text inizde bulunacak olan karakterleri alta ekleyebilirsiniz
	static String Letter = "0123456789.,abcçdefgðhýijklmnoöprsþtuüvyzABCÇDEFGÐHIÝJKLMNOÖPRSÞTUÜVYZ";

	public static void main(String[] args) {
		// txt belgelerindeki türkçe kelimeleri liste haline getirip trCharcer listemize
		// atýyoruz.
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
		// þifreleme yapmak için alfabeyi elle ekledim
		for (int i = 0; i < Letter.length(); i++) {
			// sayýdan harf çaðýrmak için
			numberToCharacter.put(i, Letter.charAt(i));
			// harfden sayý çaðýrmak için
			characterToNumber.put(Letter.charAt(i), i);
		}

		

		// offset vererek metnimizi  þifreliyoruz
		String sezarSifreli = caesarEncryption("sima", 7);
		System.out.println("þifreli metin: " + sezarSifreli);
		// sezarla þifrelenmiþ metini kýrmayý denemekteyiz
		String sezarKir = decodeText(sezarSifreli);
		System.out.println(sezarKir);

		// bu kýsýmda text i Ybs key i ile þifreliyoruz
		String s = encryptWithKey("çalýþýyoruz.", "Ybs");
		// çýkan þifrelenmiþ metin
		System.out.println("þifreli metin :" + s);
		// ayný key ile þifreyi kýrýyoruz
		System.out.println(decryptWith(s, "Ybs"));
	}
	public static String decodeText(String sifreli) {
		String enteredPassword = "";
		String[] encryptedList = sifreli.split(" ");
		int offset = 0;
	
		// 26 seferlik bir loop aldýk
		while (offset < 27) {
			// cümlenin ilk kelimesini kýrmayý deniyoruz sonuç alýrsak þifre çözülmüþ
			// oluyor
			String s1 = caesarDecrypt(encryptedList[0], offset);
			if (trCharcer.contains(s1.toLowerCase())) {
				enteredPassword += s1 + " ";

			} else {
				enteredPassword = "";
				offset += 1;

			}

			// eðer kýrýlan þifre degiþkeni boþ degilse bir kelime bulunmuþ demektir bu
			// aþamada tahmini ofsetimiz çýkmýþ oluyor
			if (enteredPassword != "") {
				// bulduðumuz ofset ile tüm metni kýrýyoruz
				enteredPassword = caesarDecrypt(sifreli, offset);
				// artýk dödgüyle iþimiz kalmadýðý için kýrýyoruz döngümüzü
				break;
			}
		}

		return "Text : " + enteredPassword + " Offset :" + offset;
	}

	// assci tablosuna göre sezar þifreleme methotumuz
	//Burada klasik bir sezar þifreleme yaptýðýmýz için açýklama gerek duymadým.

	public static String caesarEncryption(String text, int offsetMod) {
		try

		{
			// ötelenecek offsetin alfabeyi taþmamasý için modunu alýyoruz
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

	// assci tablosuna göre þifrelenmiþ sezar þifresi kýrma methotumuz
	// þifreleme methotundan tek farký - çýkartma iþlemi olan kýsýmlardýr
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


	// bu methot 2. kýsým içindir
	private static String encryptWithKey(String text, String key) {
		String ciphertext = "";
		int offset = 0;
		// gelen key  stringin her bir karakterini listeye atýyoruz
		char[] keyToCharList = key.toCharArray();
		// gelen text stringinin her bir karakterini  listeye atýyoruz
		char[] textToCharList = text.toCharArray();

		// text in her bir karakterini þifrelemek için for loop a sokuyoruz
		for (int i = 0; i < textToCharList.length; i++) {
			// key ile text in o indisindeki karakterlerin numberOfCharacters map indeki karþýlýk
			// gelen rakamlarý birbirinden çýkartýyoruz
			offset = characterToNumber.get(textToCharList[i]) + characterToNumber.get(keyToCharList[i % keyToCharList.length]);

			if (offset != 0)
				ciphertext += numberToCharacter.get(offset % numberToCharacter.size());
			else
				ciphertext += numberToCharacter.get(offset);
		}
		// þifreledigimiz metni geri return ediyoruz
		return ciphertext;

	}

	// bu methot 2. kýsým içindir
	private static String decryptWith(String text, String key) {
		String encrypted = "";
		int offset = 0;
		// gelen key in her bir karakterini asci kodunu listeye attým
		char[] keyToCharList = key.toCharArray();
		// gelen text in her bir karakterini asci kodunu listeye attým
		char[] textToCharList = text.toCharArray();

		// text in her bir karakterini þifrelemek için for loop a sokuyoruz
		for (int i = 0; i < textToCharList.length; i++) {
			// key ile text in o indisindeki karakterlerin asciAlfabe map indeki karþýlýk
			// gelen rakamlarý birbirinden çýkartýyoruz
			offset = characterToNumber.get(textToCharList[i]) - characterToNumber.get(keyToCharList[i % keyToCharList.length]);

			if (offset != 0) {
			if (offset > 0)
				encrypted += numberToCharacter.get(offset % numberToCharacter.size());
			else // ofset "-" deðeri alýrsa alfabe boyutu ile  toplama iþlemi yaptýrýyoruz. Bu sayede sondan geriye gelmiþ oluyrouz.
				encrypted += numberToCharacter.get(numberToCharacter.size()+(offset % numberToCharacter.size()));
			}else// 0 ýn mod unu almýyoruz. Çünkü 0 hiçbir sayýya bölünmemez.
				encrypted += numberToCharacter.get(offset);
		}
		// þifreledigimiz metini return ediyoruz.
		return encrypted;

	}

	
}

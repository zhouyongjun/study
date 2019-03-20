package com.zhou.test.translate;

//Imports the Google Cloud client library
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

/**
 * Windows ≈‰÷√£∫
 * With PowerShell:
	$env:GOOGLE_APPLICATION_CREDENTIALS="[PATH]"
	For example:
	$env:GOOGLE_APPLICATION_CREDENTIALS= "C:\\Users\\username\\Downloads\\[FILE_NAME].json"	
	With command prompt:
	set GOOGLE_APPLICATION_CREDENTIALS=[PATH]
	
 * LINUX OR MACOS≈‰÷√£∫
	export GOOGLE_APPLICATION_CREDENTIALS="[PATH]"
	For example:
	export GOOGLE_APPLICATION_CREDENTIALS="/home/user/Downloads/[FILE_NAME].json"
 * @author zhouyongjun
 *
 */
public class GoogleTranslateTest {
public static void main(String... args) throws Exception {
 // Instantiates a client
 Translate translate = TranslateOptions.getDefaultInstance().getService();

 // The text to translate
 String text = "Hello, world!";

 // Translates some text into Russian
 Translation translation =
     translate.translate(
         text,
         TranslateOption.sourceLanguage("en"),
         TranslateOption.targetLanguage("ru"));


 System.out.printf("Text: %s%n", text);
 System.out.printf("Translation: %s%n", translation.getTranslatedText());
}
}
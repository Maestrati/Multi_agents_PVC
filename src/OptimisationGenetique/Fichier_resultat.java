package OptimisationGenetique;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class Fichier_resultat {
public static String content =Main.content;
public static int [][] matricechemin;


/* renvoie la matrice de string sur un fihcier txt à partir 
 * duquel je transforme sur Python ma matrice  en matrice de float 
 * et en tire les graphes que je souhaite (je trouvais les graphes plus
 *  esthétiques sur Python).  */

	private static final String FILENAME = "Resultat.txt";

	
	
	public static void main(String[] args)throws IOException {
			
			String R=Main.resultatspourgraphe(200);
			 System.out.print(R);
			BufferedWriter bw = null;
			FileWriter fw =null;

			try {

				content =R;

				fw = new FileWriter(FILENAME);
				bw = new BufferedWriter(fw);
				bw.write(content); //écrit dans le fichier Resultat.txt créé

				System.out.println("Done");

			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {

					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}

			}

		}

	}


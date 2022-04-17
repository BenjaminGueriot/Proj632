package projet;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import com.opencsv.*;


public class webscrapping {
	
	//listes des genres permettant de les différentier des fonctionnalité
	private static ArrayList<String> listGenres = new ArrayList<String>(Arrays.asList("Action", "Aventure","Indépendant","Jeux de rôle","Stratégie","Monde ouvert","Jeux de tir","Casse-tête",
			"Première personne","Narration","Simulation","Casual","Tour par tour","Exploration","Horreur","Jeux de plateforme","Party game","Survie","Culture générale","Construction de villes",
			"Infiltration","Combat","Comédie","Action-aventure","Course","Rogue-lite","Jeux de cartes","Sport","Dungeon Crawler"));
	

	public static void main(String[] args) {
		
		String url_epic = "https://www.epicgames.com/store/fr/browse?sortBy=title&sortDir=ASC&count=80&start=";
		
		try {
			
			//creation du fihier csv + mise en place du mécanisme d'écriture
			File file = new File("C:\\Users\\benji\\Desktop\\doc_cours\\IDU3\\S6\\proj Data\\programme\\doc.csv");
		    FileWriter outputfile = new FileWriter(file);
		    CSVWriter writer = new CSVWriter(outputfile);
		  
		    //ajout de l'en-tête au csv
		    String[] tete = { "Name", "Price", "genres", "features","developer", "publisher", "platform", "Release date","note critique","moyenne avis","Classification OpenCritique"};
		    writer.writeNext(tete);
		  
		    //Connection a la boutique des jeux
			for (int i = 0; i < 961; i+= 80) {

				String url_epic2 = url_epic + i;
				Document doc = Jsoup.connect(url_epic2).get();
	
				//--------------------------------------Récupération des infos-----------------------------------------------------
				
				int count = 0;
				for (Element element : doc.select("li.css-lrwy1y")) {
					
					if(count == 80) {}
					else {
						count+=1;
						
						// initialisation des variables d'information
						String name;
						String price;
						String genre = "";
						String feature = "";
						String developer = "";
						String publisher = "";
						String platform = "";
						String release_date = "";
						String noteCritique ="";
						String moyenneAvis = "";
						String ClassificationOC = "";
						ArrayList<String> listPlatform = new ArrayList<String>();
						
						name  = element.select("div.css-1h2ruwl").text();
						price = element.select("span.css-z3vg5b").text();
							
						//--------------------------------------Récupération des liens-----------------------------------------------------
							
						Elements link = element.select("a.css-1jx3eyg");
						String full_link = "https://store.epicgames.com" + link.attr("href");
						Document doc2 = Jsoup.connect(full_link).get();
						
						//Récupération des genres et focntionalités
						for (Element element2 : doc2.select("a.css-1672chc")) {
							
							if(element2.select("span.css-z3vg5b").text().equals("")) {
									
							}
							else {
									
								if(listGenres.contains(element2.select("span.css-z3vg5b").text())) {
									
									if(genre == "") {
										genre = element2.select("span.css-z3vg5b").text();
									}
									else {
										genre = genre + " " + element2.select("span.css-z3vg5b").text();
									}
									
								}
								else {	
									if(feature == "") {
										feature = element2.select("span.css-z3vg5b").text();
									}
									else {
										feature = feature + " " + element2.select("span.css-z3vg5b").text();
									}

								}
							}
						}

						// Récupération des développeur, éditeur et date de sortie
						int counter = 0;
						for(Element element3 : doc2.select("div.css-fxdlmq")) {
								
								
									
							if(counter == 3) {
									
							}
							else if(element3.select("span.css-z3vg5b").text().equals("")) {
										
							}
							else {
									
								counter += 1;
									
								switch(counter) {
								case 1:
									developer = element3.select("span.css-z3vg5b").text();
									break;
								case 2:
									publisher = element3.select("span.css-z3vg5b").text();
									break;
								case 3 :
									release_date = element3.select("span.css-z3vg5b").text();
									break;
								default:
								}		
							}
						}
								
						// Récupération de la platforme
						for(Element element4 : doc2.select("ul.css-e6kwg0")) {
									
							if((element4.select("span.css-1qwdmuy").text().equals("")) || (listPlatform.contains(element4.select("span.css-1qwdmuy").text()))){
										
							}
							else {
										
								if(platform == "") {
									platform = element4.select("span.css-1qwdmuy").text();
									listPlatform.add(element4.select("span.css-1qwdmuy").text());
								}
								else {
									platform = platform + " " + element4.select("span.css-1qwdmuy").text();
									listPlatform.add(element4.select("span.css-1qwdmuy").text());
								}
							}	
						}
						
						
						//Récupération des notes
						int counter2 = 0;
						for(Element element5 : doc2.select("ul.css-1qfz72n")) {
							
							if(counter2 == 3) {
								
							}
							else if((element5.select("div.css-1q9chu").text().equals(""))){
										
							}
							else {
								
								counter2 += 1;
									
								switch(counter2) {
								case 1:
									noteCritique = element5.select("div.css-1q9chu").text();
									System.out.println(element5.select("div.css-1q9chu").text());
									break;
								case 2:
									moyenneAvis = element5.select("div.css-1q9chu").text();
									break;
								case 3 :
									ClassificationOC = element5.select("div.css-1q9chu").text();
									break;
								default:
								}
							}				
						}

						/*	
						System.out.println(name);
						System.out.println(price);
						System.out.println(genre);
						System.out.println(feature);
						System.out.println(developer);
						System.out.println(publisher);
						System.out.println(platform);
						System.out.println(release_date);
						System.out.println(noteCritique);
						System.out.println(moyenneAvis);
						System.out.println(ClassificationOC);
						System.out.println("\n");
						*/
							
						// ajoute d'un jeu dans le fichier csv
						String[] jeu = {name,price,genre,feature,developer,publisher,platform,release_date,noteCritique,moyenneAvis,ClassificationOC};
						writer.writeNext(jeu);
					}
				}	
			}
			
			// ferme le mécanisme d'écriture
	        writer.close();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
}

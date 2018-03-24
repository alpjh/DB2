package DBProject4;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		JdbcHelper dbm = new JdbcHelper();

		String NAME, ID = "default", PW, oID;
		String a_title, a_singer, a_genre;
		String m_title;
		int m_cmd, m_num;

		Scanner scanner = new Scanner(System.in);
		int login = 0; // admin : 1 , user : 2
		int cmd;
		int adminOrUser;
		int cmdA, cmdU;

		while (login != 1 || login != 2) {

			////////////////////// LOGIN && REGIST//////////////////////////////
			if (login == 0) {

				System.out.println("====================\nMusicPlayer\n====================\n0. Login\n1. Register"
						+ "\n2. Exit\nInput : ");

				try {
					cmd = scanner.nextInt();
				}
				catch(Exception e) {
					cmd = -1;
				}
				scanner.nextLine();

				switch (cmd) {
				case 0:
					// Login
					System.out.println("Admin or User? Input Admin : 1 , User : 2");
					try{
						adminOrUser = scanner.nextInt();
						scanner.nextLine();
					}
					catch(Exception e) {
						adminOrUser = 0;
						System.out.println("InputError!");
						continue;
					}

					if (adminOrUser != 1 && adminOrUser != 2) {
						System.out.println("Error! Retry Please");
						continue;
					}
					System.out.print("Input ID : ");

					ID = scanner.nextLine();
					System.out.print("Input PassWord : ");
					PW = scanner.nextLine();

					if (dbm.login(ID, PW, adminOrUser)) {
						if (adminOrUser == 1) {// if admin
							login = 1;
							System.out.println("Login Success! You are Admin!");
						} else {// if user
							System.out.println("Login Success! You are User!");
							login = 2;
						}
					} else {
						System.out.println("Login Fail!");
						System.out.println("retry please..");
						continue;
					}
					continue;

				case 1:// Register
					System.out.println("====================\nRegister\n====================");
					System.out.print("Input NAME : ");
					NAME = scanner.nextLine();
					System.out.print("Input ID : ");
					ID = scanner.nextLine();
					System.out.print("Input PassWord : ");
					PW = scanner.nextLine();
					dbm.insert(NAME, ID, PW);
					continue;
				case 2:
					System.exit(0);
					break;
				default:
					System.out.println("\nInputError!");
					continue;
				}
				break;

			}

			////////////////////// AFTER LOGIN //////////////////////////
			///////// ADMIN/////////

			else if (login == 1) {// if admin
				System.out.println("====================\nADMIN MENU\n====================");
				System.out.println("0. Logout\n1. View User List\n2. Delete User\n3. Register Album\n4. Delete Album\n"
						+ "5. Register Music\n6. Delete Music");
				try {
					cmdA = scanner.nextInt();
				}
				catch(Exception e) {
					System.out.println("Input Error!");
					continue;
				}
				scanner.nextLine();

				switch (cmdA) {
				case 0:// logout
					login = 0;
					System.out.println("Logout Success!");
					continue;
				case 1:// View User
					dbm.select("user");
					continue;
				case 2:
					System.out.print("Input user ID : ");
					oID = scanner.next();
					System.out.println(oID);
					dbm.delete("delete from user where (ID = '"+oID+"')");

					System.out.println("success");
					continue;
				case 3:// Register Album
					System.out.print("Input Album Title : ");
					a_title = scanner.nextLine();
					System.out.print("Input Album Artist");
					a_singer = scanner.nextLine();
					System.out.println("Input Album Genre");
					a_genre = scanner.nextLine();
					dbm.insert(a_title, a_singer, a_genre, "dummy");
					dbm.albumSelect("album");
					continue;

				case 4:// Delete Album
					int a_num;
					dbm.albumSelect("album");
					System.out.println("Input Album ID To Delete(-1 Show Album List)");
					try{
					a_num = scanner.nextInt();
					}
					catch(Exception e) {
						a_num = -1;
					}
					if(a_num == -1) 
						dbm.albumSelect("album");
					else
						dbm.delete("delete from album where (a_num = "+a_num+")");
					continue;
					
				case 5:// Register Music
					System.out.print("Input Music Title : ");
					m_title = scanner.next();
					while(true){
						try {
							System.out.println("Input Album ID(Show Album List : -1) : ");
							m_cmd = scanner.nextInt();
						} catch (Exception e) {
							// TODO: handle exception
							System.out.println("Input Error!");
							continue;
						} 
						
						switch (m_cmd) {
						case -1:
							dbm.albumSelect("album");
							continue;
						default:					
							break;
						}	
						break;
						
					}
					
					dbm.insert(m_title, m_cmd);
					dbm.update("update album set num_of_tracks = num_of_tracks + 1 where a_num = "+m_cmd+"");
					continue;
		
				case 6:// Delete Music
					while(true) {
						try{
							System.out.print("Input Music ID To Delete(-1 Show Music List) : ");
							m_num = scanner.nextInt();
						}
						catch(Exception e) {
							System.out.println("Input Error!");
							continue;
						}
						if(m_num == -1) {
							System.out.println(" ID\tTITLE\t\t\t\tARTIST\t\t\tGENRE\t\t\tALBUM\t\t\tPOPULARITY\n"
									+ "----------------------------------------------------------------------------------------"
									+ "---------------------------------------");
							dbm.showMusicList("select m.m_num id, m.title title, a.artist artist, a.genre genre, a.title atitle, m.popularity popularity " +
			                    "from music m, album a where m.a_num=a.a_num");
							continue;
						}
						else {
							dbm.deleteMusic(m_num);
							break;
						}
					}
				default:
					break;
				}
			}

			
			////////////////////////////// USER/////////////////////////////////
			else if (login == 2) {// if user
				System.out.println("====================\nUSER MENU\nID : " + ID + ""
						+ "\nFavoriteGenre : " + dbm.getFavoriteGenre(ID)
						+ "\n====================");
				
				System.out.println("0. Logout\n1. Show Music List\n2. Show PlayList\n3. Add PlayList"
						+ "\n4. Remove PlayList\n5. Show HotTrack\n6. Recommend Music\n7. Scoring Music");
				try{
				cmdU = scanner.nextInt();
				}
				catch(Exception e) {
					System.out.println("Input Error!");
					continue;
				}
				switch(cmdU) {
				case 0 ://LOGOUT
					login = 0;
					continue;
					
				case 1 ://SHOW MUSIC LIST
					String sql = "select m.m_num id, m.title title, a.artist artist, a.genre genre, a.title atitle, m.popularity popularity " +
		                    "from music m, album a where m.a_num=a.a_num";
					
					System.out.println(" ID\tTITLE\t\t\t\tARTIST\t\t\tGENRE\t\t\tALBUM\t\t\tPOPULARITY\tSCORE\n"
							+ "----------------------------------------------------------------------------------------------"
							+ "---------------------------------------");
					dbm.showMusicList(sql);
					
					continue;
				
				case 2 ://SHOW PLAYLIST
					
					sql = "select m.m_num id, m.title title, a.artist artist, a.genre genre, a.title atitle, m.popularity popularity " +
		                    "from playlist p, music m, album a where p.user_id ='"+ID+"' and p.music_id=m.m_num and m.a_num=a.a_num";
					
					System.out.println(" ID\tTITLE\t\t\t\tARTIST\t\t\tGENRE\t\t\tALBUM\t\t\tPOPULARITY\tSCORE\n"
							+ "----------------------------------------------------------------------------------------------"
							+ "---------------------------------------");
					dbm.showMusicList(sql);
					
					break;
					
				case 3 ://ADD PLAYLIST 
					m_num = 0;
					while(m_num != -2) {
						System.out.print("Input Music ID(if view list : -1, if done : -2) : ");
						try {
							m_num = scanner.nextInt();
						} catch (Exception e) {
							// TODO: handle exception
							m_num = -1;
							System.out.println("Input Error!");
						}
						switch(m_num) {
						case -1:
							sql = "select m.m_num id, m.title title, a.artist artist, a.genre genre, a.title atitle, m.popularity popularity " +
				                    "from music m, album a where m.a_num=a.a_num";
							
							System.out.println(" ID\tTITLE\t\t\t\tARTIST\t\t\tGENRE\t\t\tALBUM\t\t\tPOPULARITY\tSCORE\n"
									+ "----------------------------------------------------------------------------------------------"
									+ "---------------------------------------");
							dbm.showMusicList(sql);
							continue;
						case -2:
							System.out.println("Add Done!");
							break;
						default : 
							dbm.insert(m_num, ID);
							continue;
						}
						
					}
					sql = "update user set favorite_genre = '"+dbm.getFavoriteGenre(ID)+"' where  id='"+ID+"'";
					dbm.update(sql);
					break;
				case 4://Delete from playlist
					m_num = 0;
					while(m_num != -2) {
						System.out.print("Input Music ID(if view list : -1, if done : -2) : ");
						try {
							m_num = scanner.nextInt();
						} catch (Exception e) {
							// TODO: handle exception
							m_num = -1;
							System.out.println("Input Error!");
						}
						switch(m_num) {
						case -1:
							sql = "select m.m_num id, m.title title, a.artist artist, a.genre genre, a.title atitle, m.popularity popularity " +
				                    "from playlist p, music m, album a where p.user_id ='"+ID+"' and p.music_id=m.m_num and m.a_num=a.a_num";
							
							System.out.println(" ID\tTITLE\t\t\t\tARTIST\t\t\tGENRE\t\t\tALBUM\t\t\tPOPULARITY\tSCORE\n"
									+ "----------------------------------------------------------------------------------------------"
									+ "---------------------------------------");
							dbm.showMusicList(sql);
							continue;
						case -2:
							System.out.println("Remove Done!");
							break;
						default : 
							dbm.delete(ID, m_num);
							continue;
						}
					}
					sql = "update user set favorite_genre = '"+dbm.getFavoriteGenre(ID)+"' where  id='"+ID+"'";
					dbm.update(sql);
					continue;
				case 5 :////SHOW HOT TRACK
					System.out.println(" ID\tTITLE\t\t\t\tARTIST\t\t\tGENRE\t\t\tALBUM\t\t\tPOPULARITY\tSCORE\n"
							+ "----------------------------------------------------------------------------------------------"
							+ "---------------------------------------");
					sql = "select distinct m.m_num id, m.title title, a.artist artist, a.genre genre, a.title atitle, m.popularity popularity " 
							+ "from playlist p, music m, album a "
		                    + "where p.music_id=m.m_num and m.a_num=a.a_num order by m.popularity desc limit 10";
					dbm.showMusicList(sql);
					continue;
					
				case 6 : //RECOMMEND MUSIC
					
					sql = "select m.title from music m, album a "
							+ "where m.a_num=a.a_num and a.genre='"+dbm.getFavoriteGenre(ID)+"' "
							+ "order by m.popularity desc";
					
					dbm.recommend(sql);
					
					continue;
					
				case 7 : /////////////////////////Score
					
					int tmp_id, tmp_score;
					
					sql = "select m.m_num id, m.title title, a.artist artist, a.genre genre, a.title atitle, m.popularity popularity " +
		                    "from playlist p, music m, album a where p.user_id ='"+ID+"' and p.music_id=m.m_num and m.a_num=a.a_num";
					
					System.out.println(" ID\tTITLE\t\t\t\tARTIST\t\t\tGENRE\t\t\tALBUM\t\t\tPOPULARITY\tSCORE\n"
							+ "----------------------------------------------------------------------------------------------"
							+ "---------------------------------------");
					dbm.showMusicList(sql);
					while (true) {
						try {
							System.out.print("Insert Music ID To Give Score : ");
							tmp_id = scanner.nextInt();
							scanner.nextLine();
							System.out.print("Insert Score(0~5) : ");
							tmp_score = scanner.nextInt();
							if (tmp_score > 5 || tmp_score < 0) {
								System.out.println("Invalid Value!");
								continue;
							}
							dbm.insert(tmp_id, tmp_score);
							break;

						} catch (Exception e) {
							System.out.println("Fail Scoring.");
							continue;
						}
					}
					
					
					break;
			
				default :
					System.out.println("Error! Retry Please!");
					continue;		
				}
			}
			
			

		}

	}

}


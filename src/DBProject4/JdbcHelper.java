package DBProject4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcHelper {
	String driver = "org.mariadb.jdbc.Driver";
	String url = "jdbc:mariadb://localhost:3306/company";
	String uId = "root";
	String uPwd = "1234";

	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;

	public JdbcHelper() {//////////////// DRIVER LOAD /////////////////////
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, uId, uPwd);

			if (con != null) {
				System.out.println("데이터 베이스 접속 성공");
			}

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로드 실패");
		} catch (SQLException e) {
			System.out.println("데이터 베이스 접속 실패");
		}
	}

	public boolean login(String id, String pw, int cmd) {//////////// LOGIN METHOD////////////////
		String sql = "select * ";

		if (cmd == 1)
			sql += "from admin where (ID = '" + id + "')";
		else
			sql += "from user where (ID = '" + id + "')";

		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			rs.next();

			if (rs.getString("pw").equals(pw)) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			return false;
		}
	}

	public void insert(String name, String id, String pw) { // insert for
															// Register
		String sql = "insert into user values('" + name + "', '" + id + "', '" + pw + "' , null)";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

		} catch (SQLException e) {
			System.out.println("Register Fail! Overlapped ID or Invalid Value");
		}
	}

	public void insert(String title, String artist, String genre, String dummy) { ////////// INSERT
																					////////// Album

		int num;

		String sql = "select * from album";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			num = rs.getFetchSize();
		} catch (Exception e) {
			System.out.println("error!");
			num = 0;
		}

		while (true) {

			sql = "select * from album where a_num = " + num + "";

			try {
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();

				if (rs.next()) {
					num++;
				} else
					break;

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		sql = "insert into album values(" + num + ", '" + title + "', '" + artist + "', '" + genre + "', 0)";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.println("Register Complete");
		} catch (SQLException e) {
			System.out.println("Register Fail!");
		}
	}

	public void insert(int m_num, String id) { ///////////////////// PLAYLIST
												///////////////////// ADD//////////

		String sql = "select * from music where m_num = " + m_num + "";

		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (!rs.next()) {
				System.out.println("Not Exist Music!");
				return;
			}
		} catch (Exception e) {

		}

		sql = "insert into playlist values('" + id + "', " + m_num + ")";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.println("Register Complete");
			sql = "update music set popularity=popularity+1 where m_num=" + m_num + "";
			this.update(sql);
		} catch (SQLException e) {
			System.out.println("Register Fail!");
		}

	}

	public void insert(String title, int a_num) {/////////////////////// INSERT
													/////////////////////// MUSIC

		int num;

		String sql = "select * from music";//////// m_num finding
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			num = rs.getFetchSize();
		} catch (Exception e) {
			System.out.println("error!");
			num = 0;
		}

		while (true) {////// m_num finding

			sql = "select * from music where m_num = " + num + "";

			try {
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();

				if (rs.next()) {
					num++;
				} else
					break;

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		sql = "insert into music values(" + num + ", '" + title + "', " + a_num + " , 0)";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.println("Register Complete");
		} catch (SQLException e) {
			System.out.println("Register Fail!");
			System.out.println("Not Exist Album!");
		}

	}
	
	public void insert(int id, int score) {//INSERT SCORE
		
		String sql = "insert into star values(" + id + ", " + score + ")";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.println("Register Complete");
		} catch (SQLException e) {
			System.out.println("Register Fail!");
			System.out.println("Not Exist Album!");
		}
	}

	public void select(String obj) {
		String sql = "select * from " + obj;
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				System.out.println("Name       	 : " + rs.getString("name"));
				System.out.println("ID   		 : " + rs.getString("id"));
				System.out.println("FavoriteGenre 	 : " + rs.getString("favorite_genre"));
				System.out.println("------------------------");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("쿼리 수행 실패");
		}
	}

	public int albumSelect(String obj) {
		int num;
		String sql = "select * from " + obj;
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			num = rs.getFetchSize();
			while (rs.next()) {
				System.out.println("----------------------");
				System.out.println("ALBUM_ID		: " + rs.getString("a_num"));
				System.out.println("ALBUM_TITLE		: " + rs.getString("title"));
				System.out.println("ARTIST			: " + rs.getString("artist"));
				System.out.println("Genre			: " + rs.getString("genre"));
				System.out.println("NUMBER_OF_TRACKS	: " + rs.getString("num_of_tracks"));
			}

			return num;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("쿼리 수행 실패");
			return 0;
		}
	}

	public void delete(String sql) {//////////////// UPDATE QUERY
		try {
			int a = pstmt.executeUpdate(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("쿼리 수행 실패");
		}
	}

	public void delete(String id, int num) {////////////////// DELETE PLAYLIST
		String sql = "delete from playlist where (user_id = '" + id + "')and(music_id=" + num + ")";
		try {
			int a = pstmt.executeUpdate(sql);

			System.out.println("Remove Success!");
			sql = "update music set popularity=popularity-1 where m_num=" + num + "";
			this.update(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("쿼리 수행 실패");
		}
	}

	public void deleteMusic(int m_num) {////////////// DELETE MUSIC
		int a_num;

		String sql = "select * from music where m_num = " + m_num + "";

		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			rs.next();

			a_num = rs.getInt("a_num");
		} catch (Exception e) {
			a_num = -1;
			System.out.println("Error! Music Not Exist!");
			return;
		}
		System.out.println("Delete Success!");


		this.update("update album set num_of_tracks = num_of_tracks - 1 where a_num = " + a_num + "");

		sql = "delete from music where (m_num = " + m_num + ")";

		try {
			int a = pstmt.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Fail to Update Album");
		}
	}

	public void update(String sql) { /////////// UPDATE QUERY

		try {
			int a = pstmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("쿼리 수행 실패");
		}

	}

	public String showMusicList(String sql) {/////////////////// SELECT LIST

		String result = "";

		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ResultSet tmp = rs;
				System.out.printf("%3s\t%-30s\t%-20s\t%-20s\t%-30s\t%-3d\t%2d\n", rs.getString("id"), rs.getString("title"),
						rs.getString("artist"), rs.getString("genre"), rs.getString("atitle"), 
						rs.getInt("popularity"), this.getScore(rs.getInt("id")));
				rs = tmp;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("쿼리 수행 실패");
			result = "FAIL";
		}

		return result;
	}
	
	public int getScore(int id) {
		String sql = "select score from star where star.m_num="+id+"";
		int total=0, num=0;
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				total += rs.getInt("score");
				num++;
			}
			if (num == 0)
				return -1;
			
			return total/num;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("쿼리 수행 실패");
			return 0;
		}
		
	}

	public String getFavoriteGenre(String id) {
		String sql = "select genre, count(*) from playlist p, music m, album a " + "where p.user_id='" + id
				+ "' and p.music_id=m.m_num and m.a_num=a.a_num group by genre " + "order by count(*) desc";

		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();

			return rs.getString("genre");

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("쿼리 수행 실패");
			return "error!";
		}

	}

	public void recommend(String sql) {

		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			try {
				for (int i = 0; i < 10; i++) {
					rs.next();
					System.out.println(rs.getString("title"));
				}
			} catch (Exception e) {
				System.out.println("No More Music!");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("쿼리 수행 실패");
		}
	}

}

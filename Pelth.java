import java.sql.*;
import java.util.Scanner;

public class pelth {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "6894";

        try {
            Connection connect = DriverManager.getConnection(url, user, password);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("============Menu selection============");
                System.out.println("1. 병원조회");
                System.out.println("2. 병원예약");
                System.out.println("3. 예약조회");
                System.out.println("4. 종료");
                System.out.print("Input: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        hospitalCheck(connect, scanner);
                        break;
                    case 2:
                        reserveHospital(connect, scanner);
                        break;
                    case 3:
                        reserveCheck(connect, scanner);
                        break;
                    case 4:
                        System.out.println("감사합니다.");
                        scanner.close();
                        connect.close();
                        return;
                    default:
                        System.out.println("다시 입력해 주세요.");
                        break;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    private static void hospitalCheck(Connection connect, Scanner scanner) throws SQLException {
        try {
            String query = "SELECT * FROM hospital_inf";
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id");
                String hname = rs.getString("사업장명");
                String state = rs.getString("영업상태");
                String location = rs.getString("소재지");
                String lname = rs.getString("시군명");

                // 여기서 출력하거나 원하는 작업 수행
                System.out.println("id = " + id + " | 사업장명 = " + hname + " | 영업상태 = " + state + " | 소재지 = " + location);
            }

            rs.close();
            st.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    private static void reserveHospital(Connection connect, Scanner scanner) throws SQLException {
        try {
            System.out.print("지역을 입력하세요: ");
            String loc = scanner.next();


            String query = "SELECT * FROM hospital_inf WHERE 시군명 = ?";
            PreparedStatement st = connect.prepareStatement(query);
            st.setString(1, loc);


            ResultSet rs = st.executeQuery();


            while (rs.next()) {
                int id = rs.getInt("id");
                String hname = rs.getString("사업장명");
                String state = rs.getString("영업상태");
                String location = rs.getString("소재지");
                String lname = rs.getString("시군명");

                // 여기서 출력하거나 원하는 작업 수행
                System.out.println("id = " + id + " | 사업장명 = " + hname + " | 시군명 = " + lname);
            }




            System.out.print("사용자 ID를 입력하세요: ");
            int memberId = scanner.nextInt();

            System.out.print("병원 ID를 입력하세요: ");
            int hospitalId = scanner.nextInt();

            // Consume the newline character left in the buffer
            scanner.nextLine();

            System.out.print("예약 시간을 입력하세요 (HH:mm): ");
            String reserveTime = scanner.nextLine();



            String reserveQuery = "INSERT INTO reservation (hospital_inf_id, member_id, time) VALUES (?, ?, ?)";
            PreparedStatement reserveStatement = connect.prepareStatement(reserveQuery);
            reserveStatement.setInt(1, hospitalId);
            reserveStatement.setInt(2, memberId);


            java.sql.Time sqlTime = java.sql.Time.valueOf(reserveTime + ":00");
            reserveStatement.setTime(3, sqlTime);


            reserveStatement.executeUpdate();

            System.out.println("병원이 예약되었습니다.");


            rs.close();
            st.close();
            reserveStatement.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    private static void reserveCheck(Connection connect, Scanner scanner) throws SQLException {
        try {

            System.out.print("사용자 ID를 입력하세요: ");
            int memberId = scanner.nextInt();


            String query = "SELECT * FROM reservation WHERE member_id = ?";
            PreparedStatement st = connect.prepareStatement(query);
            st.setInt(1, memberId);


            ResultSet rs = st.executeQuery();


            while (rs.next()) {
                int id = rs.getInt("id");
                int hid = rs.getInt("hospital_inf_id");
                String time = rs.getString("time");

                // Print here or do whatever you want
                System.out.println(memberId+ " 님의 예약 내역입니다.");
                System.out.println("Reservation number = " + id + " | hospital_id = " + hid + " | member_id = " + memberId + " | Reservation time = " + time);
            }

            rs.close();
            st.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}
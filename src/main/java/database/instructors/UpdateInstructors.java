package database.instructors;

import scraping.GetRatings;
import scraping.models.Instructor;
import utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UpdateInstructors {

  public static ArrayList<Instructor> instructorUpdateList(Connection conn)
      throws SQLException {
    PreparedStatement stmt =
        conn.prepareStatement("SELECT id, name FROM instructors");
    ResultSet rs = stmt.executeQuery();
    ArrayList<Instructor> instructors = new ArrayList<>();
    while (rs.next()) {
      instructors.add(new Instructor(rs.getInt("id"), rs.getString("name")));
    }
    return instructors;
  }

  public static void updateInstructors(Connection conn,
                                       Iterable<Instructor> instructors,
                                       Integer batchSizeNullable)
      throws SQLException {
    PreparedStatement stmt =
        conn.prepareStatement("UPDATE instructors SET "
                              + "rmp_rating = ?, rmp_tid  = ? WHERE id = ?");
    GetRatings.getRatings(instructors.iterator(), batchSizeNullable)
        .filter(rating -> rating.rmpTeacherId != -1 && rating.rating != -1.0f)
        .forEach(rating -> {
          try {
            if (Utils
                    .setArray(stmt, rating.rating, rating.rmpTeacherId,
                              rating.instructorId)
                    .executeUpdate() != 1) {
              throw new RuntimeException("what the heck");
            }
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        });
  }
}

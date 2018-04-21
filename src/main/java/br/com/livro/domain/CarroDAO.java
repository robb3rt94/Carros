package br.com.livro.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class CarroDAO extends BaseDAO{
	 public Carro getCarroById(Long id) throws SQLException {
		 Connection conn = null;
		 PreparedStatement stmt = null;
		 try {
			 conn = (Connection) getConnection();
			 stmt = (PreparedStatement) conn.clientPrepareStatement("select * from carro where id=?");
			 stmt.setLong(1, id);
			 ResultSet rs = stmt.executeQuery();
			 if(rs.next()) {
				 Carro c = createCarro(rs);
				 rs.close();
				 return c;
			 }
		 } finally {
			 if(stmt != null) {
				 stmt.close();
			 }
		 }
		 return null;
	 }
	 
	 public List<Carro> findByName(String name) throws SQLException {
		 List<Carro> carros = new ArrayList<>();
		 Connection conn = null;
		 PreparedStatement stmt = null;
		 try {
			 conn = (Connection) getConnection();
			 stmt = (PreparedStatement) conn.prepareStatement("select * from carro where lower(nome) like ?");
			 stmt.setString(1, "%" + name.toLowerCase() + "%");
			 ResultSet rs = stmt.executeQuery();
			 while(rs.next()) {
				 carros.add(createCarro(rs));
			 }
			 rs.close();
		 } finally {
			 if (stmt != null) {
				 stmt.close();
			 }
			 if(conn != null) {
				 conn.close();
			 }
		 }
		 return carros;
	 }
	 
	 public List<Carro> findByTipo(String tipo) throws SQLException {
		 List<Carro> carros = new ArrayList<>();
		 Connection conn = null;
		 PreparedStatement stmt = null;
		 try {
			 conn = (Connection) getConnection();
			 stmt = (PreparedStatement) conn.prepareStatement("select * from carro where tipo = ?");
			 stmt.setString(1, tipo);
			 ResultSet rs = stmt.executeQuery();
			 while(rs.next()) {
				 carros.add(createCarro(rs));
			 }
			 rs.close();
		 } finally {
			 if (stmt != null) {
				 stmt.close();
			 }
			 if(conn != null) {
				 conn.close();
			 }
		 }
		 return carros;
	 }
	 
	 public List<Carro> getCarros() throws SQLException{
		 List<Carro> carros = new ArrayList<>();
		 Connection conn = null;
		 PreparedStatement stmt = null;
		 try {
			 conn = (Connection) getConnection();
			 stmt = (PreparedStatement) conn.prepareStatement("select * from carro");
			 ResultSet rs = stmt.executeQuery();
			 while(rs.next()) {
				 carros.add(createCarro(rs));
			 }
			 rs.close();
		 } finally {
			 if (stmt != null) {
				 stmt.close();
			 }
			 if(conn != null) {
				 conn.close();
			 }
		 }
		 return carros;
	 }
	 
	 public Carro createCarro(ResultSet rs) throws SQLException {
		 Carro c = new Carro();
		 c.setId(rs.getLong("id"));
		 c.setNome(rs.getString("nome"));
		 c.setDesc(rs.getString("descricao"));
		 c.setUrlFoto(rs.getString("url_foto"));
		 c.setUrlVideo(rs.getString("url_video"));
		 c.setLatitude(rs.getString("latitude"));
		 c.setLongitude(rs.getString("longitude"));
		 c.setTipo(rs.getString("tipo"));
		 return c;
	 }
	 
	 public void save(Carro c) throws SQLException {
		 Connection conn = null;
		 PreparedStatement stmt = null;
		 try {
			 conn = (Connection) getConnection();
			 if(c.getId() == null) {
				 stmt = (PreparedStatement) conn.prepareStatement("insert into carro (nome,descricao,url_foto,url_video,latitude,longitude,tipo)"
						 + "VALUES(?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
			 } else {
				 stmt = (PreparedStatement) conn.prepareStatement("update carro set nome=?,descricao=?,url_foto=?,url_video=?,latitude=?,longitude=?,tipo=? where id=?");
			 }
			 stmt.setString(1, c.getNome());
			 stmt.setString(2, c.getDesc());
			 stmt.setString(3, c.getUrlFoto());
			 stmt.setString(4, c.getUrlVideo());
			 stmt.setString(5, c.getLatitude());
			 stmt.setString(6, c.getLongitude());
			 stmt.setString(7, c.getTipo());
			 
			 if(c.getId() != null) {
				 //Update
				 stmt.setLong(8, c.getId());
			 }
			 
			 int count = stmt.executeUpdate();
			 if(count == 0) {
				 throw new SQLException("Erro ao inserir carro");
			 }
			 if(c.getId() == null) {
				 c.setId(getGeneratedId(stmt));				 
			 }
			 
		 } finally {
			 if (stmt != null) {
				 stmt.close();
			 }
			 if(conn != null) {
				 conn.close();
			 }
		 }
	 }
	 
	 public static Long getGeneratedId(Statement stmt) throws SQLException {
		 ResultSet rs = stmt.getGeneratedKeys();
		 if(rs.next()) {
			 return rs.getLong(1);
		 }
		 return 0L;
	 }
	 
	 public boolean delete(Long id) throws SQLException {
		 Connection conn = null;
		 PreparedStatement stmt = null;
		 try {
			 conn = (Connection) getConnection();
			 stmt = (PreparedStatement) conn.prepareStatement("delete from carro where id = ?");
			 stmt.setLong(1, id);
			 int count = stmt.executeUpdate();
			 return count > 0;
			 
		 } finally {
			 if (stmt != null) {
				 stmt.close();
			 }
			 if(conn != null) {
				 conn.close();
			 }
		 }
	 }
}

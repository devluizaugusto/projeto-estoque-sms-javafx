package model.dao.impl;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.ProductDao;
import model.entities.Product;
import model.entities.Setores;

public class ProductDaoJDBC implements ProductDao {

	private Connection conn;

	public ProductDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Product obj) {
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(
					"INSERT INTO produto "
					+ "(Nome, DataEntrada, QtdEntrada, QtdSaida, QtdTotal, DataSaida, IdSetor) "
					+ "VALUES " 
					+ "(?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
		
			ps.setString(1, obj.getNome());
			ps.setDate(2, new java.sql.Date(obj.getDataEntrada().getTime()));
			ps.setInt(3, obj.getQtdEntrada());
			ps.setInt(4, obj.getQtdSaida());
			ps.setInt(5, obj.getQtdTotal());
			ps.setDate(6, new java.sql.Date(obj.getDataSaida().getTime()));
			ps.setInt(7, obj.getSetor().getId());
			
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("UNEXPECTED ERROR! NO ROWS AFFECTED!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
		}

	}

	@Override
	public void update(Product obj){
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(
					"UPDATE produto SET Nome = ?, DataEntrada = ?, QtdEntrada = ?, QtdSaida = ?, QtdTotal = ?, DataSaida = ?, IdSetor = ? WHERE Id = ?");
					
			ps.setString(1, obj.getNome());
			ps.setDate(2, new java.sql.Date(obj.getDataEntrada().getTime()));
			ps.setInt(3, obj.getQtdEntrada());
			ps.setInt(4, obj.getQtdSaida());
			ps.setInt(5, obj.getQtdTotal());
			ps.setDate(6, new java.sql.Date(obj.getDataSaida().getTime()));
			ps.setInt(7, obj.getSetor().getId());
			ps.setInt(8, obj.getId());
			
			ps.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(
					"DELETE FROM produto WHERE Id = ?");
			
			ps.setInt(1, id);
			ps.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
		}
	}

	@Override
	public Product findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(
					"SELECT produto.*,setor.Nome as SectNome " 
					+ "FROM produto INNER JOIN setor "
					+ "ON produto.IdSetor = setor.Id " 
					+ "WHERE produto.Id = ?");

			ps.setInt(1, id);

			rs = ps.executeQuery();

			if (rs.next()) {
				Setores sect = instantiateSetor(rs);
				Product obj = instantiateProduto(rs, sect);
				return obj;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

	private Product instantiateProduto(ResultSet rs, Setores sect) throws SQLException {
		Product obj = new Product();
		obj.setId(rs.getInt("Id"));
		obj.setNome(rs.getString("Nome"));
		obj.setDataEntrada(new java.util.Date(rs.getTimestamp("DataEntrada").getTime()));
		obj.setQtdEntrada(rs.getInt("QtdEntrada"));
		obj.setQtdSaida(rs.getInt("QtdSaida"));
		obj.setQtdTotal(rs.getInt("QtdTotal"));
		obj.setDataSaida(new java.util.Date(rs.getTimestamp("DataSaida").getTime()));
		obj.setSetor(sect);
		return obj;
	}

	private Setores instantiateSetor(ResultSet rs) throws SQLException {
		Setores sect = new Setores();
		sect.setId(rs.getInt("IdSetor"));
		sect.setNome(rs.getString("SectNome"));
		return sect;
	}

	@Override
	public List<Product> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT produto.*,setor.Nome as SectNome " 
					+ "FROM produto INNER JOIN setor "
					+ "ON produto.IdSetor = setor.Id " 
					+ "ORDER BY Nome");

			rs = st.executeQuery();

			List<Product> list = new ArrayList<>();
			Map<Integer, Setores> map = new HashMap<>();

			while (rs.next()) {

				Setores sect = map.get(rs.getInt("IdSetor"));

				if (sect == null) {
					sect = instantiateSetor(rs);
					map.put(rs.getInt("IdSetor"), sect);
				}

				Product obj = instantiateProduto(rs, sect);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Product> findBySetor(Setores setor) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT produto.*,setor.Nome as SectNome " 
					+ "FROM produto INNER JOIN setor "
					+ "ON produto.IdSetor = setor.Id " 
					+ "WHERE IdSetor = ? " 
					+ "ORDER BY Nome");

			st.setInt(1, setor.getId());

			rs = st.executeQuery();

			List<Product> list = new ArrayList<>();
			Map<Integer, Setores> map = new HashMap<>();

			while (rs.next()) {

				Setores sect = map.get(rs.getInt("IdSetor"));

				if (sect == null) {
					sect = instantiateSetor(rs);
					map.put(rs.getInt("IdSetor"), sect);
				}

				Product obj = instantiateProduto(rs, sect);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
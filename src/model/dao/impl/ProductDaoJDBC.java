package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.ProductDao;
import model.entities.Product;

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
					"INSERT INTO produtos " + "(Nome, QtdEntrada, QtdSaida, QtdTotal) " + "VALUES " + "(?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, obj.getNome());
			ps.setInt(2, obj.getQtdEntrada());
			ps.setInt(3, obj.getQtdSaida());
			ps.setInt(4, obj.getQtdTotal());

			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
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
	public void update(Product obj) {
		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(
					"UPDATE produtos " + "SET Nome = ?, QtdEntrada = ?, QtdSaida = ?, QtdTotal = ? " + " WHERE Id = ?");

			ps.setString(1, obj.getNome());
			ps.setInt(2, obj.getQtdEntrada());
			ps.setInt(3, obj.getQtdSaida());
			ps.setInt(4, obj.getQtdTotal());
			ps.setInt(5, obj.getId());

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
			ps = conn.prepareStatement("DELETE FROM produtos WHERE Id = ?");

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
			ps = conn.prepareStatement("SELECT * FROM produtos WHERE Id = ?");

			ps.setInt(1, id);

			rs = ps.executeQuery();

			if (rs.next()) {
				Product obj = new Product();
				obj.setId(rs.getInt("Id"));
				obj.setNome(rs.getString("Nome"));
				obj.setQtdEntrada(rs.getInt("QtdEntrada"));
				obj.setQtdEntrada(rs.getInt("QtdSaida"));
				obj.setQtdEntrada(rs.getInt("QtdTotal"));
				return obj;
			}
			return null;
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
	}

	@Override
	public List<Product> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("SELECT * FROM produtos ORDER BY Nome");

			rs = ps.executeQuery();

			List<Product> list = new ArrayList<>();

			while (rs.next()) {
				Product obj = new Product();
				obj.setId(rs.getInt("Id"));
				obj.setNome(rs.getString("Nome"));
				obj.setQtdEntrada(rs.getInt("QtdEntrada"));
				obj.setQtdSaida(rs.getInt("QtdSaida"));
				obj.setQtdTotal(rs.getInt("QtdTotal"));
				list.add(obj);
			}
			return list;
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
	}
}

package model.entities;

import java.io.Serializable;

public class Product implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nome;
	private Integer qtdEntrada;
	private Integer qtdSaida;
	private Integer qtdTotal;
	
	public Product() {
	}

	public Product(Integer id, String nome, Integer qtdEntrada, Integer qtdSaida, Integer qtdTotal) {
		this.id = id;
		this.nome = nome;
		this.qtdEntrada = qtdEntrada;
		this.qtdSaida = qtdSaida;
		this.qtdTotal = qtdTotal;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getQtdEntrada() {
		return qtdEntrada;
	}

	public void setQtdEntrada(Integer qtdEntrada) {
		this.qtdEntrada = qtdEntrada;
	}

	public Integer getQtdSaida() {
		return qtdSaida;
	}

	public void setQtdSaida(Integer qtdSaida) {
		this.qtdSaida = qtdSaida;
	}

	public Integer getQtdTotal() {
		return qtdTotal;
	}

	public void setQtdTotal(Integer qtdTotal) {
		this.qtdTotal = qtdTotal;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", nome=" + nome + ", qtdEntrada=" + qtdEntrada + ", qtdSaida=" + qtdSaida
				+ ", qtdTotal=" + qtdTotal + "]";
	}
}

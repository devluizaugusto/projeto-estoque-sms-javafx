package model.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Product implements Serializable{
	
	private static final long serialVersionUID = 1L;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	private Integer id;
	private String nome;
	private Date dataEntrada;
	private Integer qtdEntrada;
	private Integer qtdSaida;
	private Integer qtdTotal;
	private Date dataSaida;
	
	private Setores setor;
	
	public Product() {
	}

	public Product(Integer id, String nome, Date dataEntrada, Integer qtdEntrada, Integer qtdSaida, Integer qtdTotal, Date dataSaida, Setores setor) {
		this.id = id;
		this.nome = nome;
		this.dataEntrada = dataEntrada;
		this.qtdEntrada = qtdEntrada;
		this.qtdSaida = qtdSaida;
		this.qtdTotal = qtdTotal;
		this.dataSaida = dataSaida;
		this.setor = setor;
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
	
	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
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
	
	public Date getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(Date dataSaida) {
		this.dataSaida = dataSaida;
	}
	
	public Setores getSetor() {
		return setor;
	}
	
	public void setSetor(Setores setor) {
		this.setor = setor;
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
		return "ID PRODUTO: " + id 
				+ "\nNOME PRODUTO: " + nome
				+ "\nDATA DE ENTRADA PRODUTO: " + sdf.format(dataEntrada)
				+ "\nQTD DE ENTRADA PRODUTO: " + qtdEntrada
				+ "\nQTD DE SAIDA PRODUTO: " + qtdSaida
				+ "\nQTD TOTAL PRODUTO: " + qtdTotal
				+ "\nDATA DE SAIDA PRODUTO: " + sdf.format(dataSaida)
				+ "\nSETOR PRODUTO: " + setor;
	}

}

package model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Receipt implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer codDocument;
	private String documentType;
	private Date issueDate;
	private Date dueDate;
	private Double value;
	private String paymentStatus;
	private String bank;
	private String payDate;
	private Customer customer;

	public Receipt() {
	}

	// OBS: o construtor não utiliza todos os atributos

	public Receipt(Integer id, Integer codDocument, String documentType, Date issueDate, Date dueDate, Double value,
			Customer customer) {

		this.id = id;
		this.codDocument = codDocument;
		this.documentType = documentType;
		this.issueDate = issueDate;
		this.dueDate = dueDate;
		this.value = value;
		this.customer = customer;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCodDocument() {
		return codDocument;
	}

	public void setCodDocument(Integer codDocument) {
		this.codDocument = codDocument;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Receipt other = (Receipt) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "|Número -> " + codDocument + " |NF/RECIBO -> " + documentType + " |Emissão -> " + issueDate
				+ " |Vencimento -> " + dueDate + " |Valor R$ -> " + value + " |Status -> " + paymentStatus
				+ " |Data pagamento -> " + payDate + " |Banco -> " + bank + " |Empresa -> " + customer.getName();

	}
}

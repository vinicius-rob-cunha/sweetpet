package br.com.sweetpet.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Entidade que representa uma consulta
 * @author Vinicius Cunha
 */
@Entity
public class Consulta {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private LocalDate data;
	
	@NotNull @Min(0) @Max(23)
	private Integer hora;
	
	@NotNull @Min(0) @Max(59)
	private Integer minuto;

	@NotEmpty
	private String detalhes;

	@NotNull
	@ManyToOne
	private Animal animal;
	
	@NotNull @Valid
	@ManyToOne
	private Veterinario veterinario;
	
	@NotNull
	private BigDecimal valor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDetalhes() {
		return detalhes;
	}

	public void setDetalhes(String detalhes) {
		this.detalhes = detalhes;
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public Veterinario getVeterinario() {
		return veterinario;
	}
	
	public void setVeterinario(Veterinario veterinario) {
		this.veterinario = veterinario;
	}

	public Integer getHora() {
		return hora;
	}

	public void setHora(Integer hora) {
		this.hora = hora;
	}

	public Integer getMinuto() {
		return minuto;
	}

	public void setMinuto(Integer minuto) {
		this.minuto = minuto;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

}

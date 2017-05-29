package br.com.sweetpet.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

/**
 * Entidade que representa um animal
 * @author Vinicius Cunha
 */
@Entity
public class Animal {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@NotEmpty
	private String nome;
	
	@Valid
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	private Cliente dono;
	
	@NotNull
	@Enumerated
	private TipoAnimal tipo;
	
	@OneToMany(mappedBy = "animal", fetch = FetchType.EAGER)
	private List<Vacina> vacinas = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Cliente getDono() {
		return dono;
	}

	public void setDono(Cliente dono) {
		this.dono = dono;
	}

	public TipoAnimal getTipo() {
		return tipo;
	}

	public void setTipo(TipoAnimal tipo) {
		this.tipo = tipo;
	}

	public List<Vacina> getVacinas() {
		return vacinas;
	}

	public void setVacinas(List<Vacina> vacinas) {
		this.vacinas = vacinas;
	}
	
	/**
	 * @return A vacina agendada com a data mais próxima
	 */
	public Vacina getProximaVacina(){
		List<Vacina> vacinas = getVacinas().stream()
										  .filter(v -> v.getStatus() == StatusVacina.AGENDADA)
										  .sorted(comparing(Vacina::getData))
										  .collect(Collectors.toList());
		return vacinas.isEmpty() ? null : vacinas.get(0);
	}
	
	/**
	 * @return Vacinas ordenadas da mais próxima para a mais antiga
	 */
	public List<Vacina> getVacinasOrdenadas(){
		return getVacinas().stream().sorted(comparing(Vacina::getData)).collect(Collectors.toList());
	}

	/**
	 * Adiciona uma vacina para o Animal
	 * @param vacina
	 */
	public void addVacina(Vacina vacina) {
		getVacinas().add(vacina);
	}
	
}

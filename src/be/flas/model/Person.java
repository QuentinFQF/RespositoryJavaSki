package be.flas.model;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Person {
	private String name;
	private String firstName;
	private int PersonId;
	private LocalDate dateOfBirth;
	private String pseudo;
	//utiliser dans skierForm
	public Person(String name,String firstName,String pseudo,LocalDate dateOfBirth) {
		this.dateOfBirth=dateOfBirth;
		this.firstName=firstName;
		this.name=name;
		
		this.pseudo=pseudo;
	}
	public Person(String name,String firstName,int personId,String pseudo) {
		
		this.firstName=firstName;
		this.name=name;
		this.PersonId=personId;
		this.pseudo=pseudo;
	}
	public Person(String name,String firstName,int personId,LocalDate dateOfBirth,String pseudo) {
		this.dateOfBirth=dateOfBirth;
		this.firstName=firstName;
		this.name=name;
		this.PersonId=personId;
		this.pseudo=pseudo;
	}
	public Person(String name,String firstName,String pseudo) {
		//this.dateOfBirth=dateOfBirth;
		this.firstName=firstName;
		this.name=name;
		
		this.pseudo=pseudo;
	}
	public Person(String name,String firstName,LocalDate dateOfBirth,String pseudo) {
		this.dateOfBirth=dateOfBirth;
		this.firstName=firstName;
		this.name=name;
		
		this.pseudo=pseudo;
	}
    public Person(int id) {
		this.PersonId=id;
	}
	public Person() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public int getPersonId() {
		return PersonId;
	}

	public void setPersonId(int personId) {
		PersonId = personId;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", firstName=" + firstName + ", PersonId=" + PersonId + ", dateOfBirth="
				+ dateOfBirth + ", pseudo=" + pseudo + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(PersonId, dateOfBirth, firstName, name, pseudo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		return PersonId == other.PersonId && Objects.equals(dateOfBirth, other.dateOfBirth)
				&& Objects.equals(firstName, other.firstName) && Objects.equals(name, other.name)
				&& Objects.equals(pseudo, other.pseudo);
	}
	
	
	

}

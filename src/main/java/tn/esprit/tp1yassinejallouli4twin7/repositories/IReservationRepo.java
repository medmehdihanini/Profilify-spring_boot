package tn.esprit.tp1yassinejallouli4twin7.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import tn.esprit.tp1yassinejallouli4twin7.entities.Etudiant;
import tn.esprit.tp1yassinejallouli4twin7.entities.Reservation;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface IReservationRepo extends CrudRepository<Reservation,Long> {
    //List<Reservation> findByDateReservationBetween(Date dateDebut,Date dateFin);



    /*nejd*/

    List<Reservation> findByAnneeUniversitaireBetween(Date debutAnnee, Date finAnnee);

    List<Reservation> findByEtudiants(Etudiant e);

    @Query("SELECT r FROM Reservation r , Chambre c, Etudiant e  WHERE c.idChambre = :idChambre AND :etudiant MEMBER OF r.etudiants AND r MEMBER OF c.reservations")
    Reservation findReservationByCinAndChambreId(@Param("etudiant") Etudiant etudiant, @Param("idChambre") Long idChambre);



    @Query("SELECT r FROM Reservation r JOIN r.etudiants e WHERE e.cin = :cin AND :thisyear <= r.debuteAnneUniversite AND r.debuteAnneUniversite <= :nextyear AND r.finAnneUniversite >= :thisyear")
    Reservation getCurrentReservationByEtudiantId(Long cin, LocalDate thisyear, LocalDate nextyear);

    Page<Reservation> findAll(Specification<Reservation> reservationSpecification, Pageable pageable);

}

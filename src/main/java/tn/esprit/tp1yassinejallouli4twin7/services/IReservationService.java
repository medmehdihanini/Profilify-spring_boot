package tn.esprit.tp1yassinejallouli4twin7.services;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.esprit.tp1yassinejallouli4twin7.DTO.ReservationDTO;
import tn.esprit.tp1yassinejallouli4twin7.entities.EtatReservation;
import tn.esprit.tp1yassinejallouli4twin7.entities.Reservation;

import java.util.Date;
import java.util.List;

public interface IReservationService {
    /*
    Reservation ajouterReservation(Reservation r);
    Reservation updateReservation(Reservation r);
    void supprimerReservation(Long idReservation);
    Reservation getReservation(Long idReservation);
    List<Reservation> getAllReservations();
    Reservation ajouterReservationEtAssignerAChambreEtAEtudiant (Reservation res, Long numChambre, long cin);
    long getReservationParAnneeUniversitaire(Date debutAnnee, Date finAnnee);
    Reservation ajouterReservation(long idChambre,long cinEtudiant);
    Reservation annulerReservation(long cinEtudiant);
*/

    Reservation AjouterReservation(Reservation r);
    Reservation UpdateReservation(Long id,String etat);
    void SupprimerReservation(long idReservation,Long cinEtudiant);
    Reservation GetReservation(long idReservation);
    List<Reservation> GetAllReservation();
    Reservation ajouterReservationEtAssignerAChambreEtAEtudiant (Reservation res, Long
            numChambre, Long cin) ;
    long getReservationParAnneeUniversitaire(Date debutAnnee, Date finAnnee ) ;
    public Reservation ajouterReservation (long idChambre, long cinEtudiant,Reservation r) ;

    @Transactional
    Reservation annulerReservation(long cinEtudiant);

    Reservation getReservationByCinEtud(Long id,Long cinEtydiant);

    Page<ReservationDTO> GetChambreFilterBy(
            String numReservation,
            EtatReservation etat,
            Long cinEtudiant,
            Pageable pageable
    );

}

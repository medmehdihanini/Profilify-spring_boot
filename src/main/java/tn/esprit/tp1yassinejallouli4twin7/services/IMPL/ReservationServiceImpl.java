package tn.esprit.tp1yassinejallouli4twin7.services.IMPL;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import tn.esprit.tp1yassinejallouli4twin7.DTO.ReservationDTO;
import tn.esprit.tp1yassinejallouli4twin7.entities.*;
import tn.esprit.tp1yassinejallouli4twin7.repositories.IBlocRepo;
import tn.esprit.tp1yassinejallouli4twin7.repositories.IChambreRepo;
import tn.esprit.tp1yassinejallouli4twin7.repositories.IEtudiantRepo;
import tn.esprit.tp1yassinejallouli4twin7.repositories.IReservationRepo;
import tn.esprit.tp1yassinejallouli4twin7.services.IReservationService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import jakarta.persistence.criteria.Join;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Primary
@AllArgsConstructor
@Service
public class ReservationServiceImpl implements IReservationService {

    IReservationRepo resrvationRepository;
    IChambreRepo chambreRepository;
    IEtudiantRepo etudiantRepository;
    IBlocRepo iblocRepository;
    @Override
    public Reservation AjouterReservation(Reservation r) {
        return resrvationRepository.save(r);
    }

    @Override
    public Reservation UpdateReservation(Long id,String etat) {

        Reservation r=this.resrvationRepository.findById(id).orElse(null);
        r.setEtat(EtatReservation.valueOf(etat));
        return resrvationRepository.save(r);
    }

    @Override
    public void SupprimerReservation(long idReservation,Long cinEtudiant) {
        Etudiant e =etudiantRepository.findEtudiantByCin(cinEtudiant);
        List<Reservation> r = resrvationRepository.findByEtudiants(e);
        for(Reservation re:r) {
            Chambre ch = chambreRepository.findChambreByReservations(re);
            ch.getReservations().remove(re);
            re.getEtudiants().remove(e);
            resrvationRepository.delete(re);
        }
    }

    @Override
    public Reservation GetReservation(long idReservation) {
        return resrvationRepository.findById( idReservation).orElse(null);
    }

    @Override
    public List<Reservation> GetAllReservation() {
        return (List<Reservation>) resrvationRepository.findAll();
    }

    @Override
    @Transactional
    public Reservation ajouterReservationEtAssignerAChambreEtAEtudiant(Reservation res, Long numChambre, Long cin) {
        Reservation resrvation = resrvationRepository.findById(res.getIdReservation()).orElse(null);
        Chambre ch=chambreRepository.findById(numChambre).orElse(null);
        if(ch.getReservations()==null){
            ch.setReservations(new HashSet<>());
        }
        ch.getReservations().add(resrvation);
        resrvation.getEtudiants().add(etudiantRepository.findEtudiantByCin(cin));
        return resrvation;
    }

    @Override
    public long getReservationParAnneeUniversitaire(Date debutAnnee, Date finAnnee) {
        List<Reservation> reservation =  resrvationRepository.findByAnneeUniversitaireBetween(debutAnnee, finAnnee);
        return reservation.size();
    }

    @Override
    public Reservation ajouterReservation(long idChambre, long cinEtudiant, Reservation r) {
        Chambre ch = chambreRepository.findById(idChambre).orElse(null);
        Etudiant et = etudiantRepository.findEtudiantByCin(cinEtudiant);

        LocalDate currentDate = LocalDate.now();
        LocalDate thisyear = currentDate;
        LocalDate nextyear = currentDate.plusYears(1);

        Reservation existingReservation = resrvationRepository.getCurrentReservationByEtudiantId(cinEtudiant, thisyear, nextyear);

        if (existingReservation != null) {
            r.setNumReservation(ch.getNumeroChambre() + "-" + ch.getBlocchambre().getNomBloc() + "-" + cinEtudiant);
            r.setIdReservation(existingReservation.getIdReservation());
            r.setEtudiants(existingReservation.getEtudiants());
            r.setEtat(EtatReservation.EN_ATTENTE);
            return resrvationRepository.save(r);
        }else{
            r.setNumReservation(ch.getNumeroChambre()+"-"+ch.getBlocchambre().getNomBloc()+"-"+cinEtudiant);
            r.setEtat(EtatReservation.EN_ATTENTE);
            int c=ch.getReservations().size();
            if(r.getEtudiants()==null){
                r.setEtudiants(new HashSet<>());
            }
            if(ch.getReservations()==null){
                ch.setReservations(new HashSet<>());
            }
            if(ch.getTypeC()==TypeChambre.SIMPLE && c<1 ){
                r=resrvationRepository.save(r);
                ch.getReservations().add(r);
                r.getEtudiants().add(et);
                chambreRepository.save(ch);
                return r;
            }
            if(ch.getTypeC()==TypeChambre.DOUBLE && c<2 ){
                r=resrvationRepository.save(r);
                ch.getReservations().add(r);
                r.getEtudiants().add(et);
                chambreRepository.save(ch);
                return r;
            }
            if(ch.getTypeC()==TypeChambre.TRIPLE && c<3 ){
                r.getEtudiants().add(et);
                r=resrvationRepository.save(r);
                ch.getReservations().add(r);
                chambreRepository.save(ch);
                return r;
            }
            return null;
        }


    }

    @Transactional
    @Override
    public Reservation annulerReservation(long cinEtudiant) {
        Etudiant e =etudiantRepository.findEtudiantByCin(cinEtudiant);
        List<Reservation> r = resrvationRepository.findByEtudiants(e);
        for(Reservation re:r) {
            Chambre ch = chambreRepository.findChambreByReservations(re);
            ch.getReservations().remove(re);
            re.getEtudiants().remove(e);
            resrvationRepository.delete(re);
        }
        return null;
    }

    @Override
    public Reservation getReservationByCinEtud(Long id,Long cinEtydiant) {
        Etudiant etudiant = etudiantRepository.findEtudiantByCin(cinEtydiant);
        if (etudiant != null) {
            return resrvationRepository.findReservationByCinAndChambreId(etudiant,id);
        }
        return null;
    }


    @Override
    public Page<ReservationDTO> GetChambreFilterBy(
            String numReservation,
            EtatReservation etat,
            Long cinEtudiant,
            Pageable pageable
    ) {
        List<ReservationDTO> listdto = new ArrayList<>();

        Page<Reservation> reservationPage = resrvationRepository.findAll((Specification<Reservation>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();


            if (numReservation != null ) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("numReservation")), "%" + numReservation.toLowerCase() + "%"));
            }

            if (etat != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("etat")), "%" + etat + "%"));
            }

            if (cinEtudiant != null) {
                Join<Reservation, Etudiant> etudiantJoin = root.join("etudiants");
                predicates.add(criteriaBuilder.like(etudiantJoin.get("cin").as(String.class), "%"+cinEtudiant + "%"));
            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        for (Reservation c : reservationPage.getContent()) {
            listdto.add(new ReservationDTO(c));
        }

        Page<ReservationDTO> reservationDtoPage = new PageImpl<>(listdto, pageable, reservationPage.getTotalElements());
        return reservationDtoPage;
    }

    /*

    @Override
    public Reservation ajouterReservation(Reservation r) {
        return reservationRepo.save(r);
    }

    @Override
    public Reservation updateReservation(Reservation r) {
        return reservationRepo.save(r);
    }

    @Override
    public void supprimerReservation(Long idReservation) {
         reservationRepo.deleteById(idReservation);
    }

    @Override
    public Reservation getReservation(Long idReservation) {
        return reservationRepo.findById(idReservation).orElse(null);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return (List<Reservation>) reservationRepo.findAll();
    }

    @Override
    public long getReservationParAnneeUniversitaire(Date debutAnnee, Date finAnnee) {
        return  reservationRepo.findByDateReservationBetween(debutAnnee,finAnnee).size();

    }

    @Override
    @Transactional
    public Reservation ajouterReservationEtAssignerAChambreEtAEtudiant(Reservation res, Long numChambre, long cin) {
        this.ajouterReservation(res);
        Chambre ch = chambreRepo.findChambreByNumeroChambre(numChambre);
        Etudiant et = etudiantRepo.findEtudiantByCin(cin);
        ch.getReservations().add(res);
        et.getReservations().add(res);
        return res;
    }


    @Override
    @Transactional
    public Reservation ajouterReservation(long idChambre, long cinEtudiant) {
        Chambre ch = chambreRepo.findById(idChambre).orElse(null);
        Etudiant et = etudiantRepo.findEtudiantByCin(cinEtudiant);

        Reservation r = new Reservation();
        r.setNumReservation(ch.getNumeroChambre() + ch.getBlocchambre().getNomBloc() + cinEtudiant);
        r.setDebuteAnneUniversite(LocalDate.parse(LocalDate.now().getYear() + "-09-01"));
        r.setFinAnneUniversite(LocalDate.parse(LocalDate.now().getYear() + 1 + "-06-01"));
        r.setEstValid(true);

        if (ch.getTypeC() == TypeChambre.SIMPLE && ch.getReservations().isEmpty()) {
            this.ajouterReservation(r);
            ch.getReservations().add(r);
            et.getReservations().add(r);
        }

        if (ch.getTypeC() == TypeChambre.DOUBLE && ch.getReservations().size() < 2) {
            this.ajouterReservation(r);
            ch.getReservations().add(r);
            et.getReservations().add(r);
        }

        if (ch.getTypeC() == TypeChambre.TRIPLE && ch.getReservations().size() < 3) {
            this.ajouterReservation(r);
            ch.getReservations().add(r);
            et.getReservations().add(r);
        }

        return r;
    }

    @Transactional
    @Override
    public Reservation annulerReservation(long cinEtudiant) {
        Etudiant e = etudiantRepo.findEtudiantByCin(cinEtudiant);


        List<Reservation> resList = new ArrayList<>(e.getReservations());

        for (Reservation res : resList) {
            Chambre ch = chambreRepo.findChambreByReservations(res);

            // Remove associations
            ch.getReservations().remove(res);
            res.getEtudiants().remove(e);

            // Update reservation state
            res.setEstValid(false);
            reservationRepo.save(res);

            // Remove reservation from the original list
            e.getReservations().remove(res);
        }

        return null;
    }
*/

}

package tn.esprit.tp1yassinejallouli4twin7.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tp1yassinejallouli4twin7.DTO.ReservationDTO;
import tn.esprit.tp1yassinejallouli4twin7.entities.EtatReservation;
import tn.esprit.tp1yassinejallouli4twin7.entities.Foyer;
import tn.esprit.tp1yassinejallouli4twin7.entities.Reservation;
import tn.esprit.tp1yassinejallouli4twin7.services.IFoyerServices;
import tn.esprit.tp1yassinejallouli4twin7.services.IReservationService;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor //2eme lezem el final mawjouda
@RequestMapping("reservation")
@RestController
@CrossOrigin(origins = "*")
public class ReservationRestController {

    final public IReservationService reservationService;

    @GetMapping("ALLReservation")
    public List<Reservation> getAllReservation() {
        return reservationService.GetAllReservation();
    }

    @PostMapping("addReservation")
    public Reservation Addbloc(@RequestBody Reservation r) {
        return reservationService.AjouterReservation(r);
    }

    @DeleteMapping("deleteReservation/{id}/{cin}")
    public void Deletefoyer(@PathVariable long id,@PathVariable Long cin) {
        reservationService.SupprimerReservation(id,cin);
    }

    @PostMapping("UpdateReservation")
    public Reservation MOdifierBlocs(@RequestParam(value = "id") Long id, @RequestParam(value = "etat") String etat) {
        return reservationService.UpdateReservation(id,etat);
    }

    @PutMapping("ajouterReservationEtAssignerAChambreEtAEtudiant/{numChambre}/{id_etudiant}")
    public Reservation ajouterReservationEtAssignerAChambreEtAEtudiant(@RequestBody Reservation r, @PathVariable long numChambre, @PathVariable long id_etudiant) {
        return reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(r, numChambre, id_etudiant);
    }

    @GetMapping("getReservationParAnneeUniversitaire/{debutAnnee}/{finAnnee}")
    public long getReservationParAnneeUniversitaire(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date debutAnnee,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date finAnnee) {
        return reservationService.getReservationParAnneeUniversitaire(debutAnnee, finAnnee);
    }

    @PostMapping("affecterChambre/{id}/{cin}")
    public Reservation affecter(@PathVariable Long id, @PathVariable Long cin,@RequestBody Reservation r) {
        return reservationService.ajouterReservation(id, cin,r);
    }

    @DeleteMapping("annulerReservation/{cin_etudiant}")
    public Reservation annulerReservation(@PathVariable long cin_etudiant) {
        return reservationService.annulerReservation(cin_etudiant);
    }

    @GetMapping("getReservationByCin/{id}/{cin}")
    public Reservation getReservationByCin(@PathVariable Long id,@PathVariable Long cin){
        return reservationService.getReservationByCinEtud(id,cin);
    }

    @GetMapping("getReservationFilter")
    public Page<ReservationDTO> getReservationFilter(
            @RequestParam(value = "numReservation", required = false) String numReservation,
            @RequestParam(value = "etat", required = false) EtatReservation etat,
            @RequestParam(value = "cinEtudiant", required = false) Long cinEtudiant,
            Pageable pageable
    ) {
        return reservationService.GetChambreFilterBy(numReservation, etat, cinEtudiant, pageable);
    }
/*
    @GetMapping("/all")
    public List<Reservation> getAllReservation(){
        return reservationService.getAllReservations();
    }

    @PostMapping("/add")
    public Reservation addReservation(@RequestBody Reservation r){
        return reservationService.ajouterReservation(r);
    }

    @PostMapping("/update")
    public Reservation updateReservation(@RequestBody Reservation r){
        return reservationService.updateReservation(r);
    }

    @DeleteMapping ("/delete/{idReservation}")
    public void deleteReservation(@PathVariable Long idReservation){
        reservationService.supprimerReservation(idReservation);
    }
    @PutMapping("/ajouterReservationEtAssignerAChambreEtAEtudiant/{numChambre}/{cin}")
    public Reservation ajouterReservationEtAssignerAChambreEtAEtudiantAPI (@RequestBody Reservation res,@PathVariable Long numChambre,@PathVariable long cin){
       return reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(res,numChambre,cin);
    }
    @PostMapping("/getReservationParAnneeUniversitaire/{debutAnnee}/{finAnnee}")
    public long getReservationParAnneeUniversitaireApi(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)Date debutAnnee,@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date finAnnee ){
       return reservationService.getReservationParAnneeUniversitaire(debutAnnee,finAnnee);
    }

    @PutMapping("/ajouterReservation/{idChambre}/{cinEtudiant}")
    public Reservation ajouterReservationApi(@PathVariable long idChambre,@PathVariable long cinEtudiant){
       return reservationService.ajouterReservation(idChambre,cinEtudiant);
    }

    @PutMapping("/annulerReservation/{cinEtudiant}")
    public Reservation annulerReservationApi(@PathVariable long cinEtudiant){
       return reservationService.annulerReservation(cinEtudiant);
    }

 */


}

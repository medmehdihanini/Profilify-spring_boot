package tn.esprit.tp1yassinejallouli4twin7.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.esprit.tp1yassinejallouli4twin7.DTO.ChambreDTO;
import tn.esprit.tp1yassinejallouli4twin7.entities.Chambre;
import tn.esprit.tp1yassinejallouli4twin7.entities.TypeChambre;

import java.util.List;

public interface IChambreService {
    Chambre ajouterChambre(Chambre ch);
    Chambre updateChambre(Chambre ch);
    void supprimerChambre(long idChambre);
    Chambre getChambre(long idChambre);
    List<Chambre> getAllChambres();
    List<Chambre> getChambresParNomBloc( String nomBloc);
    long nbChambreParTypeEtBloc(TypeChambre type, long idBloc) ;
    List<Chambre> getChambresNonReserveParNomFoyerEtTypeChambre( String nomFoyer,TypeChambre type);
    List<Chambre> getChambresParBlocEtType(long idBloc, TypeChambre typeC);

    List<Chambre>getChambresNonAffecter();


    Chambre getChambreParId(long id);

    Page<ChambreDTO> GetChambreFilterBy(
            String nom,
            String NomBloc,
            Long idFoyer,
            Pageable pageable
    );

    Chambre getChambreById(Long id);


}

package tn.esprit.tp1yassinejallouli4twin7.services.IMPL;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import tn.esprit.tp1yassinejallouli4twin7.DTO.ChambreDTO;
import tn.esprit.tp1yassinejallouli4twin7.entities.*;
import tn.esprit.tp1yassinejallouli4twin7.repositories.IBlocRepo;
import tn.esprit.tp1yassinejallouli4twin7.repositories.IChambreRepo;
import tn.esprit.tp1yassinejallouli4twin7.repositories.IFoyerRepo;
import tn.esprit.tp1yassinejallouli4twin7.repositories.IReservationRepo;
import tn.esprit.tp1yassinejallouli4twin7.services.IChambreService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import jakarta.persistence.criteria.Join;

import javax.management.Query;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ChambreServiceImpl implements IChambreService {

    IChambreRepo chambreRepo;
    IBlocRepo blocRepo;
    IFoyerRepo foyerRepo;
    IReservationRepo reservationRepo;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Chambre ajouterChambre(Chambre ch) {
        return chambreRepo.save(ch);
    }

    @Override
    public Chambre updateChambre(Chambre ch) {
        return chambreRepo.save(ch);
    }

    @Override
    public void supprimerChambre(long idChambre) {
        chambreRepo.deleteById(idChambre);
    }

    @Override
    public Chambre getChambre(long idChambre) {
        return chambreRepo.findById(idChambre).orElse(null);
    }

    @Override
    public List<Chambre> getAllChambres() {
        return (List<Chambre>) chambreRepo.findAll();
    }

    @Override
    public List<Chambre> getChambresParNomBloc(String nomBloc) {
        Bloc b = blocRepo.findBlocByNomBloc(nomBloc);
        return new ArrayList<>(b.getChambres());
    }

    @Override
    public long nbChambreParTypeEtBloc(TypeChambre type, long idBloc) {
        Bloc b = blocRepo.findById(idBloc).orElse(null);
        int nb=0;
        if(b!=null){
            for(Chambre ch :b.getChambres())
                if(ch.getTypeC()==type) nb++;
            return nb;
        }
        return 0;
    }

    @Override
    public List<Chambre> getChambresNonReserveParNomFoyerEtTypeChambre(String nomFoyer, TypeChambre type) {
        Foyer f = foyerRepo.findFoyerByNomFoyer(nomFoyer);

        List<Chambre> NonResChambreList = new  ArrayList<>();
        f.getBlocs().forEach(bloc -> {
            bloc.getChambres().forEach(chambre -> {
                if (chambre.getTypeC()==type && chambre.getReservations().isEmpty()){
                    NonResChambreList.add(chambre);
                }
            });
        });
        return NonResChambreList;
    }
    @Override
    public List<Chambre> getChambresParBlocEtType(long idBloc, TypeChambre typeC) {
        Bloc b = blocRepo.findById(idBloc).orElse(null);

        return  chambreRepo.findByBlocchambreAndTypeC(b,typeC);  }

    @Override
    public List<Chambre>getChambresNonAffecter(){
        return chambreRepo.findChambreNonAffecter();
    }


    @Override
    public Chambre getChambreParId(long id) {
        return chambreRepo.findById(id).orElse(null);
    }

    @Override
    public Page<ChambreDTO> GetChambreFilterBy(
            String nom,
            String NomBloc,
            Long idFoyer,
            Pageable pageable
    ) {
        List<ChambreDTO> listdto = new ArrayList<>();

        Page<Chambre> chambrePage = chambreRepo.findAll((Specification<Chambre>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();


            if (nom != null && !nom.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nom")), "%" + nom.toLowerCase() + "%"));
            }

            if (NomBloc != null) {
                Join<Chambre, Bloc> categoryJoin = root.join("blocchambre");
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(categoryJoin.get("nomBloc")), "%" + NomBloc.toLowerCase() + "%"));
            }

            if (idFoyer != null) {
                Join<Chambre, Bloc> blocJoin = root.join("blocchambre");
                Join<Bloc, Foyer> foyerJoin = blocJoin.join("foyer");
                predicates.add(criteriaBuilder.equal(foyerJoin.get("idFoyer"), idFoyer));
            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        for (Chambre c : chambrePage.getContent()) {
            listdto.add(new ChambreDTO(c));
        }

        Page<ChambreDTO> produitsDtoPage = new PageImpl<>(listdto, pageable, chambrePage.getTotalElements());
        return produitsDtoPage;
    }

    @Override
    public Chambre getChambreById(Long id) {
        return chambreRepo.findById(id).orElse(null);
    }
}

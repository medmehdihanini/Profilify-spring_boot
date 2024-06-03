package tn.esprit.tp1yassinejallouli4twin7.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import tn.esprit.tp1yassinejallouli4twin7.entities.*;

import java.util.List;

public interface IChambreRepo extends CrudRepository<Chambre,Long> {
    Chambre findChambreByNumeroChambre(Long numero);
    Chambre findChambreByReservations(Reservation r);

    List <Chambre> findByTypeCAndBlocchambreAndReservations(TypeChambre type, Bloc bloc, Reservation reservation);

    List<Chambre> findByBlocchambreAndTypeC(Bloc bloc, TypeChambre type);
    @Query("SELECT c FROM Chambre c WHERE c.blocchambre = :bloc AND c.typeC = :type")
    List<Chambre> findByBlocAndTypeChambre(@Param("bloc") Bloc bloc, @Param("type") TypeChambre type);

    @Query("select c from Chambre c where c.blocchambre IS NULL")
    List<Chambre>findChambreNonAffecter();
/*nejd*/
    Page<Chambre> findAll(Specification<Chambre> chambreSpecification, Pageable pageable);



}

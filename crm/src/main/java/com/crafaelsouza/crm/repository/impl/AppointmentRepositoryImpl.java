package com.crafaelsouza.crm.repository.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.crafaelsouza.crm.dto.ErrorDTO;
import com.crafaelsouza.crm.entity.Appointment;
import com.crafaelsouza.crm.exception.CRMException;
import com.crafaelsouza.crm.repository.AppointmentRepository;

/**
 * The Class AppointmentRepositoryImpl.
 * 
 * @author Carlos Souza
 */
@Repository("appointmentRepository")
public class AppointmentRepositoryImpl extends AbstractCustomCrudRepository<Appointment, Integer>
	implements AppointmentRepository {

	/** The Constant MIN_RATING. */
	private static final Integer MIN_RATING = 1;
	
	/** The Constant MAX_RATING. */
	private static final Integer MAX_RATING = 5;
	
	/** The validator. */
	@Autowired
	private Validator validator;

	/** The entity manager. */
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Instantiates a new appointment repository impl.
	 *
	 * @param validator the validator
	 * @param entityManager the entity manager
	 */
	@Autowired
	public AppointmentRepositoryImpl(Validator validator, EntityManager entityManager) {
		super();
		this.validator = validator;
		this.entityManager = entityManager;
	}
	
	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.repository.AppointmentRepository#findNextWeeks(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Appointment> findNextWeeks(Integer numberWeeksAhead) {
		Date currentDate = new Date();
		LocalDate localDate = LocalDate.now();
		localDate = localDate.plusWeeks(numberWeeksAhead);
		Date dateWeeksAhead = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		StringBuilder strQuery = new StringBuilder();
		strQuery.append("SELECT a FROM Appointment a ");
		strQuery.append("WHERE a.date BETWEEN :currentDate AND :DateWeeksAhead ");
		strQuery.append("ORDER BY a.date ");
		Query query = getEntityManager().createQuery(strQuery.toString());
		query.setParameter("currentDate", currentDate);
		query.setParameter("DateWeeksAhead", dateWeeksAhead);
		return query.getResultList();
	}

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.repository.AppointmentRepository#findNextByCustomer(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Appointment findNextByCustomer(Integer customerId) {
		Date currentDate = new Date();
		StringBuilder strQuery = new StringBuilder();
		strQuery.append("SELECT a FROM Appointment a ");
		strQuery.append("WHERE a.date > :currentDate ");
		strQuery.append("AND a.customer.id = :customerId ");
		strQuery.append("ORDER BY a.date ");
		Query query = getEntityManager().createQuery(strQuery.toString());
		query.setParameter("currentDate", currentDate);
		query.setParameter("customerId", customerId);
		List<Appointment> resultList = query.getResultList();
		
		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.repository.impl.AbstractCustomCrudRepository#getValidator()
	 */
	@Override
	protected Validator getValidator() {
		return validator;
	}

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.repository.impl.AbstractCustomCrudRepository#getEntityManager()
	 */
	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.repository.impl.AbstractCustomCrudRepository#validateAndApplyChangesInOldEntity(com.crafaelsouza.crm.entity.EntityModel, com.crafaelsouza.crm.entity.EntityModel)
	 */
	@Override
	protected void validateAndApplyChangesInOldEntity(Appointment oldEntity,
			Appointment partialEntity) throws CRMException {
		validateDateAppointment(oldEntity);
		if (partialEntity.getRatingDescription()!= null) {
			oldEntity.setRatingDescription(partialEntity.getRatingDescription());
		}
		if (partialEntity.getRating() != null) {
			oldEntity.setRating(partialEntity.getRating());
		}
	}


	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.repository.AppointmentRepository#rateAppointment(com.crafaelsouza.crm.entity.Appointment)
	 */
	@Override
	public Appointment rateAppointment(Appointment appointmentRated) throws CRMException {
		validateConstraintRatingAppointment(appointmentRated);
		return partialUpdate(appointmentRated);
	}

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.repository.AppointmentRepository#findAllByCustomer(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Appointment> findAllByCustomer(Integer customerId) {
		StringBuilder strQuery = new StringBuilder();
		strQuery.append("SELECT a FROM Appointment a ");
		strQuery.append("WHERE a.customer.id = :customerId ");
		strQuery.append("ORDER BY a.date ");
		Query query = getEntityManager().createQuery(strQuery.toString());
		query.setParameter("customerId", customerId);
		List<Appointment> resultList = query.getResultList();
		return resultList;
		
	}

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.repository.impl.AbstractCustomCrudRepository#validateRulesBeforeSaving(com.crafaelsouza.crm.entity.EntityModel)
	 */
	@Override
	protected void validateRulesBeforeSaving(Appointment entity)
			throws CRMException {
		if (entity.getDate().before(new Date())) {
			throw new CRMException(new ErrorDTO("You cannot create an appointment with a date less than today."));
		}
	}

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.repository.AppointmentRepository#findByIdAndCustomer(java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Appointment findByIdAndCustomer(Integer customerId, Integer appointId) {
		StringBuilder strQuery = new StringBuilder();
		strQuery.append("SELECT a FROM Appointment a ");
		strQuery.append("WHERE a.id = :appointId ");
		strQuery.append("AND a.customer.id = :customerId ");
		Query query = getEntityManager().createQuery(strQuery.toString());
		query.setParameter("customerId", customerId);
		query.setParameter("appointId", appointId);
		List<Appointment> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}



	/**
	 * Validate date appointment for rating creation.
	 * If the appointment date is greater than now, an error message is thrown.
	 *
	 * @param entity the entity
	 * @throws CRMException the exception
	 */
	private void validateDateAppointment(Appointment entity) throws CRMException {
		if (entity.getDate().after(new Date())) {
			throw new CRMException(new ErrorDTO("You cannot rate a future appointment."));
		}
		
	}
	
	/**
	 * Validate constraint rating appointment.
	 *
	 * @param appointmentRated the appointment rated
	 * @throws CRMException the crm exception
	 */
	private void validateConstraintRatingAppointment(Appointment appointmentRated) throws CRMException {
		List<String> errors = new ArrayList<String>();
		if (appointmentRated.getRatingDescription() == null) {
			errors.add("'ratingDescription' cannot be null");
		}
		if (appointmentRated.getRating() == null) {
			errors.add("'ratinng' cannot be null");
		} else if (appointmentRated.getRating() < MIN_RATING || appointmentRated.getRating() > MAX_RATING) {
			errors.add("'ratinng' need to be between " + MIN_RATING + " and " + MAX_RATING);
		}
		if (!errors.isEmpty()) {
			throw new CRMException(new ErrorDTO(errors));
		}
	}

}

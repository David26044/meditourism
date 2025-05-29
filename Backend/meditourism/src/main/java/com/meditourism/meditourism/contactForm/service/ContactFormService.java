package com.meditourism.meditourism.contactForm.service;

import com.meditourism.meditourism.blockedUser.service.BlockedUserService;
import com.meditourism.meditourism.blockedUser.service.IBlockedUserService;
import com.meditourism.meditourism.contactForm.dto.ContactFormRequestDTO;
import com.meditourism.meditourism.contactForm.dto.ContactFormResponseDTO;
import com.meditourism.meditourism.contactForm.dto.TreatmentContactCountDTO;
import com.meditourism.meditourism.contactForm.entity.ContactFormEntity;
import com.meditourism.meditourism.contactForm.repository.ContactFormRepository;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.exception.UnauthorizedAccessException;
import com.meditourism.meditourism.treatment.entity.TreatmentEntity;
import com.meditourism.meditourism.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para manejar las operaciones relacionadas con los formularios de contacto.
 */
@Service
public class ContactFormService implements IContactFormService {

    @Autowired
    private ContactFormRepository contactFormRepository;

    @Autowired
    private IBlockedUserService blockedUserService;

    /**
     * Obtiene todos los formularios de contacto existentes.
     *
     * @return Lista de DTOs con la información de todos los formularios de contacto
     */
    @Override
    public List<ContactFormResponseDTO> getAllContactForms() {
        return ContactFormResponseDTO.fromEntityList(contactFormRepository.findAll());
    }

    /**
     * Obtiene todos los formularios de contacto asociados a un tratamiento específico.
     *
     * @param id ID del tratamiento
     * @return Lista de DTOs con los formularios de contacto del tratamiento
     * @throws ResourceNotFoundException si no se encuentran formularios para el tratamiento
     */
    @Override
    public List<ContactFormResponseDTO> getAllContactFormsByTreatmentId(Long id) {
        List<ContactFormEntity> contactForms = contactFormRepository.findAllByTreatmentId(id);

        if (contactForms.isEmpty()) {
            throw new ResourceNotFoundException("No hay formularios de contacto relacionados al tratamiento con ID: " + id);
        }

        return ContactFormResponseDTO.fromEntityList(contactForms);
    }

    /**
     * Obtiene todos los formularios de contacto asociados a un usuario específico.
     *
     * @param id ID del usuario
     * @return Lista de DTOs con los formularios de contacto del usuario
     * @throws ResourceNotFoundException si no se encuentran formularios para el usuario
     */
    @Override
    public List<ContactFormResponseDTO> getContactFormByUserId(Long id) {
        List<ContactFormEntity> contactForms = contactFormRepository.findAllByUserId(id);

        if (contactForms.isEmpty()) {
            throw new ResourceNotFoundException("No hay formularios relacionados al usuario con ID: " + id);
        }
        return ContactFormResponseDTO.fromEntityList(contactForms);
    }

    /**
     * Obtiene un formulario de contacto específico por su ID.
     *
     * @param id ID del formulario de contacto
     * @return DTO con la información del formulario de contacto
     * @throws ResourceNotFoundException si no se encuentra el formulario
     */
    @Override
    public ContactFormResponseDTO getContactFormById(Long id) {
        return new ContactFormResponseDTO(contactFormRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el formulaio de contacto con ID: "+ id)));
    }

    /**
     * Obtiene el conteo de contactos por tratamiento.
     *
     * @return Lista de DTOs con el ID del tratamiento, su nombre y el conteo de contactos
     */
    public List<TreatmentContactCountDTO> getTreatmentContactCounts() {
        List<Object[]> results = contactFormRepository.findTreatmentContactCounts();
        return results.stream()
                .map(obj -> new TreatmentContactCountDTO(
                        ((Number) obj[0]).longValue(),
                        (String) obj[1],
                        ((Number) obj[2]).longValue()))
                .toList();
    }

    /**
     * Guarda un nuevo formulario de contacto.
     *
     * @param dto DTO con la información del formulario a guardar
     * @return DTO con la información del formulario guardado
     * @throws UnauthorizedAccessException si el usuario está bloqueado
     */
    @Override
    public ContactFormResponseDTO saveContactForm(ContactFormRequestDTO dto) {
        // Validar solo si el usuario no es nulo
        if (dto.getUserId() != null && blockedUserService.isBlocked(dto.getUserId())) {
            throw new UnauthorizedAccessException("No tienes permisos");
        }

        ContactFormEntity entity = new ContactFormEntity();

        // Set user reference only if userId is provided
        if (dto.getUserId() != null) {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(dto.getUserId());
            entity.setUser(userEntity);
        }

        // Set treatment reference
        TreatmentEntity treatmentEntity = new TreatmentEntity();
        treatmentEntity.setId(dto.getTreatmentId());
        entity.setTreatment(treatmentEntity);

        // Map all fields
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setInquiryType(dto.getInquiryType());
        entity.setPreferredClinic(dto.getPreferredClinic());
        entity.setMessage(dto.getMessage());
        entity.setAcceptTerms(dto.getAcceptTerms());
        entity.setAcceptMarketing(dto.getAcceptMarketing());

        return new ContactFormResponseDTO(contactFormRepository.save(entity));
    }

    /**
     * Actualiza un formulario de contacto existente.
     *
     * @param id ID del formulario a actualizar
     * @param contactFormRequestDTO DTO con la información actualizada
     * @return DTO con la información del formulario actualizado
     * @throws ResourceNotFoundException si no se encuentra el formulario
     */
    @Override
    public ContactFormResponseDTO updateContactForm(Long id, ContactFormRequestDTO contactFormRequestDTO) {
        ContactFormEntity entity = contactFormRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No existe el formulario de contacto con ID: "+ id));
        if(contactFormRequestDTO.getEmail() != null) {
            entity.setEmail(contactFormRequestDTO.getEmail());
        }
        if(contactFormRequestDTO.getMessage() != null) {
            entity.setMessage(contactFormRequestDTO.getMessage());
        }
        return new ContactFormResponseDTO(contactFormRepository.save(entity));
    }

    /**
     * Elimina un formulario de contacto.
     *
     * @param id ID del formulario a eliminar
     * @return DTO con la información del formulario eliminado
     * @throws ResourceNotFoundException si no se encuentra el formulario
     */
    @Override
    public ContactFormResponseDTO deleteContactForm(Long id) {
        ContactFormEntity entity = contactFormRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el formulario de contacto con ID: "+ id));
        contactFormRepository.delete(entity);
        return new ContactFormResponseDTO(entity);
    }
}
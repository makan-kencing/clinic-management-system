Project Overview
================

| [Project Description](#project-description) | [Requirements](#requirements) |
|---------------------------------------------|-------------------------------|

## Project Description

This system is created for an on-campus clinic, with the main goal of providing medical
care for students in an orderly fashion.

### What is the project?

The project is about building a clinic management system for an on-campus clinic that provides 
profession consultation and medical treatments to students and outsider alike. 

The internal system, manned by a staff, should provide the tools to manage patients information, 
and provide immediate walk-in consultations or future appointments for more specialized cases. 
The system should be able to delegate consultations to appropriate physicians on shift, and 
allowing doctors to write up a detailed diagnosis and prescriptions and keep it in the patient's 
historical record, as it's expected to have mostly repeating patients in the form of university 
students. In the case of no suitable physician on shift, it would select the closest available 
timeslot for an appointment, where the patient can decide on the appropriate time of reservation.

The system should also act as a partial HR system, where it provides the management of doctors' shift 
and allocation, and integrates into the system for the assignment of appointment timeslots.

The system also acts as a partial inventory system, keeping track of the stock in and out of medications
and drugs, providing appropriate substitutes for compatible drugs, and an audit log of the current
stockage of medications.

#### Example Flow

> Assumptions
> - clinical care is provided free of charge
> - there are repeated patients

A few example flows:

##### Patient registration

1. Patient registers at the reception desk.
   - If first time registering, reception staff inquire about patient information and create a patient 
     record.
2. Patient is asked for what kind of consultations.
3. If no prior appointment is made, the patient will be making a walk-in consultation.
   - If it's for an appointment, the patient will just wait until called by the doctor. (stop here)
4. If the doctor is reasonably available, the patient is added to the waiting queue and waits at the lobby.
   - If the required doctor is not available, proceed to [scheduling appointment](#scheduling-appointment).

##### Queue processing

1. When the doctor is ready, the doctor can instruct for the next patient. (manual)
2. The doctor will check if there is an appointment scheduled for the current time.
3. If there is none, the next patient in queue is called to the doctor's office. (remove from queue)
   - If there is, the doctor will instruct for the appointed patient to come in.

##### Consultation

1. The doctor ask questions and consult the patient about their condition.
   - The doctor can also go through the patient's prior medical record for making decision.
2. The doctor notes down some of the condition (disease, syndrome) as diagnoses.
3. The doctor can provide treatments for the symptoms of some diagnoses, which creates prescriptions
   to treat the symptom.
4. The doctor records all the information as a consultation record and pass off the prescription
   to the dispensary.
5. The patient waits at the waiting room (add to queue) for the prescription to be dispensed.
6. If the doctor deems the patient in need of further revisits, the doctor will 
   [schedule an appointment](#scheduling-appointment).

##### Dispensing Medication

1. The dispensary process each patient in queue.
2. The dispensary collects the required medications and drugs for the prescription, prioritising 
   the oldest stock.
   - If exact match can't be found, a suitable substitute can be used.
3. The stock of the medication is deducted from the inventory.
4. Once the prescriptions are fully ready, the dispensary calls for the patient to collect the 
   prescriptions. (remove from queue)

##### Scheduling Appointment

1. A suitable doctor is chosen for the appointment.
2. A suitable timeslot is chosen for the appointment, prioritising the closest time or patient 
   requested time.
   - Occupied doctor can't have two appointments overlap within the same timeframe.
3. The appointment is then scheduled.

##### Drugs Stock In

1. The new batch of stock is recorded.
2. The stock is related with the exact medication or drug, its quantity, and the stock in date.

##### Managing Doctors

1. The staff enters the information for a doctor record.
2. If the doctor record doesn't exist, it will be created, else updated.

##### Assigning Doctor Shifts

1. The staff chooses the doctor to be assigned shifts.
2. The doctor shifts will be listed.
3. The staff add a shift, choosing the type, day, from time, and thru time.
4. The shift is checked for clashing shifts.
   - If it clashed, the clashing timeslot will be shown with appropriate message.
5. The shift is checked for appointments within the timeframe.
   - If an appointment is scheduled, the clashing appointment will be shown with appropriate 
     message.
6. The shift is then added to the schedule. 
   - If it's continuous with another shift of the same type, it will be extended instead.

### What is the MVP?

The minimal viable product is a system that can achieve all the flow mentioned above with
minimal issues. 

### What are the nice to haves?

The nice to haves for this project are: 
- general styling of the user interface and a more aesthetically pleasing output.
- automatic / dynamic appropriate defaults for most selection.

### When will the project be complete?

The project will be complete once all the essential features are implemented and the user interface is
at a pretty usable level.

## Requirements

All module **MUST** include a reporting feature

<sub>Modules are mandatory otherwise specified.

### Patient Management Modules

- create first-time patient records
- view patient records
- edit patient records
- clinic queue

### Doctor Management Module

- create doctor information record
- view doctor information record
- edit doctor information record
- create doctor shift
- view doctor shift
- edit doctor shift

### Consultation Management Module

- create appointment
  - allocate appropriate appointment time
- view appointment
- reschedule appointment

### Medical Treatment Management Module

- create consultation record
- create diagnoses for a consultation
- create treatments for diagnosis
- prescribe prescription for treatment
- view patient consultations, diagnoses, treatments, prescriptions history

### Pharmacy Management Module

- dispensary queue
- deduct medication stock on prescription
- add new medication stock
- view medication stocks
- show medication substitutes

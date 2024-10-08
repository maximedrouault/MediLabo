CREATE TABLE IF NOT EXISTS patients
(
    id                  bigint generated by default as identity primary key,
    address             varchar(255),
    date_of_birth       date not null,
    first_name          varchar(100) not null,
    gender              char         not null
                        constraint patients_gender_check
                        check (gender = ANY (ARRAY ['M'::bpchar, 'F'::bpchar])),
    last_name           varchar(100) not null,
    telephone_number    varchar(25)
);

INSERT INTO patients (last_name, first_name, date_of_birth, gender, address, telephone_number)
VALUES  ('TestNone', 'Test', '1966-12-31', 'F', '1 Brookside St', '100-222-3333'),
        ('TestBorderline', 'Test', '1945-06-24', 'M', '2 High St', '200-333-4444'),
        ('TestInDanger', 'Test', '2004-06-18', 'M', '3 Club Road', '300-444-5555'),
        ('TestEarlyOnset', 'Test', '2002-06-28', 'F', '4 Valley Dr', '400-555-6666');
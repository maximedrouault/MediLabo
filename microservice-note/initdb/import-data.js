db = db.getSiblingDB('notedb');

const notes = [
  {
    "patientId": 1,
    "creationDateTime": new Date("2021-02-01T07:13:12Z"),
    "patientName": "TestNone",
    "noteContent": "Le patient déclare qu'il se sent très bien. Poids égal ou inférieur au poids recommandé."
  },
  {
    "patientId": 2,
    "creationDateTime": new Date("2021-03-15T08:27:23Z"),
    "patientName": "TestBorderline",
    "noteContent": "Le patient déclare qu'il ressent beaucoup de stress au travail. Il se plaint également que son audition est anormale dernièrement."
  },
  {
    "patientId": 2,
    "creationDateTime": new Date("2022-07-20T09:42:47Z"),
    "patientName": "TestBorderline",
    "noteContent": "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois. Il remarque également que son audition continue d'être anormale."
  },
  {
    "patientId": 3,
    "creationDateTime": new Date("2022-11-10T08:21:53Z"),
    "patientName": "TestInDanger",
    "noteContent": "Le patient déclare qu'il fume depuis peu."
  },
  {
    "patientId": 3,
    "creationDateTime": new Date("2023-01-25T07:53:36Z"),
    "patientName": "TestInDanger",
    "noteContent": "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière. Il se plaint également de crises d’apnée respiratoire anormales. Tests de laboratoire indiquant un taux de cholestérol LDL élevé."
  },
  {
    "patientId": 4,
    "creationDateTime": new Date("2023-05-06T12:14:11Z"),
    "patientName": "TestEarlyOnset",
    "noteContent": "Le patient déclare qu'il lui est devenu difficile de monter les escaliers. Il se plaint également d’être essoufflé. Tests de laboratoire indiquant que les anticorps sont élevés. Réaction aux médicaments."
  },
  {
    "patientId": 4,
    "creationDateTime": new Date("2024-02-18T13:26:46Z"),
    "patientName": "TestEarlyOnset",
    "noteContent": "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps."
  },
  {
    "patientId": 4,
    "creationDateTime": new Date("2024-04-11T14:37:26Z"),
    "patientName": "TestEarlyOnset",
    "noteContent": "Le patient déclare avoir commencé à fumer depuis peu. Hémoglobine A1C supérieure au niveau recommandé."
  },
  {
    "patientId": 4,
    "creationDateTime": new Date("2024-08-05T07:08:52Z"),
    "patientName": "TestEarlyOnset",
    "noteContent": "Taille, Poids, Cholestérol, Vertige et Réaction."
  }
];

notes.forEach(note => db.notes.insert(note));
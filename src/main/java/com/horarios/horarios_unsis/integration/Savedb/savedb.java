    public void guardarAulasEnDB(List<Map<String, Object>> aulas) {

        aulas.forEach(aula -> {
            AulaEntity entity = new AulaEntity();
            entity.setClave((Integer) aula.get("clave"));
            entity.setNombre((String) aula.get("nombre"));
            entity.setCapacidad((Integer) aula.get("capacidad"));
            entity.setTipo((String) aula.get("tipo"));
            entity.setProyector((String) aula.get("proyector"));

            aulaRepository.save(entity);
        });
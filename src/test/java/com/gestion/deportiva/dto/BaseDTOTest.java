package com.gestion.deportiva.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class BaseDTOTest {

	private static class DummyDTO extends BaseDTO {
		private static final long serialVersionUID = 1L;

		public DummyDTO() {
			super();
		}

		public DummyDTO(Long id, String uuid) {
			super(id, uuid);
		}

		public DummyDTO(Long id) {
			super(id);
		}

		public DummyDTO(String uuid) {
			super(uuid);
		}
	}

	@Test
	void shouldCreateWithDefaultConstructor() {
		BaseDTO dto = new DummyDTO();
		assertNull(dto.getId());
		assertNull(dto.getUuid());
	}

	@Test
	void shouldCreateWithIdAndUuidConstructor() {
		BaseDTO dto = new DummyDTO(1L, "uuid-test");
		assertEquals(1L, dto.getId());
		assertEquals("uuid-test", dto.getUuid());
	}

	@Test
	void shouldCreateWithIdConstructor() {
		BaseDTO dto = new DummyDTO(2L);
		assertEquals(2L, dto.getId());
		assertNull(dto.getUuid());
	}

	@Test
	void shouldCreateWithUuidConstructor() {
		BaseDTO dto = new DummyDTO("uuid-test-2");
		assertNull(dto.getId());
		assertEquals("uuid-test-2", dto.getUuid());
	}

	@Test
	void shouldSettersAndGettersWork() {
		BaseDTO dto = new DummyDTO();
		dto.setId(10L);
		dto.setUuid("uuid-set");

		assertEquals(10L, dto.getId());
		assertEquals("uuid-set", dto.getUuid());
	}
}
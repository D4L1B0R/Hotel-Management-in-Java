package UnitTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import funkcionalnosti.MenadzerGosti;
import podaci.Pol;
import prikaz.IspisZapis;

class MenadzerGostiTest {
	
	MenadzerGosti menadzerGosti = new MenadzerGosti();
	IspisZapis main = new IspisZapis();
	@Test
	void testPronadjiGosta() {
		main.zapisPodataka();
		assertEquals("anči", menadzerGosti.pronadjiGosta("anči").getKorisnickoIme());
	}
	@Test
	void testPromenaPrezimenaGosta() {
		main.zapisPodataka();
		assertEquals("Anić", menadzerGosti.pronadjiGosta("anči").getPrezime());
		menadzerGosti.promenaPrezimenaGosta("anči", "Kostic");
		assertEquals("Kostic", menadzerGosti.pronadjiGosta("anči").getPrezime());
	}
	@Test
	void testPromenaImenaGosta() {
		main.zapisPodataka();
		assertEquals("anči", menadzerGosti.pronadjiGosta("anči").getKorisnickoIme());
		menadzerGosti.promenaImenaGosta("anči", "Ana");
		assertEquals("Ana", menadzerGosti.pronadjiGosta("anči").getIme());
	}
	@Test
	void testPromenaPolaGosta() {
		main.zapisPodataka();
		assertEquals("ZENSKI", menadzerGosti.pronadjiGosta("anči").getPol().toString());
		menadzerGosti.promenaPolaGosta("anči", Pol.MUSKI);
		assertEquals("MUSKI", menadzerGosti.pronadjiGosta("anči").getPol().toString());
	}
	@Test
	void testPromenaDatumaRodjenjaGosta() {
		main.zapisPodataka();
		assertEquals("23.10.1977.", menadzerGosti.pronadjiGosta("anči").getDatumRodjenja());
		menadzerGosti.promenaDatumaRodjenjaGosta("anči", "02.02.2001.");
		assertEquals("02.02.2001.", menadzerGosti.pronadjiGosta("anči").getDatumRodjenja());
	}
	@Test
	void testPromenaTelefonaGosta() {
		main.zapisPodataka();
		assertEquals(6666666667L, menadzerGosti.pronadjiGosta("anči").getTelefon());
		menadzerGosti.promenaTelefonaGosta("anči", 654321);
		assertEquals(654321, menadzerGosti.pronadjiGosta("anči").getTelefon());
	}
	@Test
	void testPromenaAdreseGosta() {
        main.zapisPodataka();
        assertEquals("Starih boraca 19, Sombor", menadzerGosti.pronadjiGosta("anči").getAdresa());
        menadzerGosti.promenaAdreseGosta("anči", "Bulevar Oslobodjenja 23");
        assertEquals("Bulevar Oslobodjenja 23", menadzerGosti.pronadjiGosta("anči").getAdresa());
	}
	@Test
	void testPromenaLozinkeGosta() {
        main.zapisPodataka();
        assertEquals("Lozinka", menadzerGosti.pronadjiGosta("anči").getLozinka());
        menadzerGosti.promenaLozinkeGosta("anči", "anči1234");
        assertEquals("anči1234", menadzerGosti.pronadjiGosta("anči").getLozinka());
	}
	@Test
	void testPromenaKorisnickogImenaGosta() {
        main.zapisPodataka();
        menadzerGosti.promenaKorisnickogImenaGosta("anči", "perke");
        assertEquals("perke", menadzerGosti.pronadjiGosta("perke").getKorisnickoIme());
	}
	@Test
	void testNadjiImeGosta() {
		main.zapisPodataka();
		assertEquals("Ana", menadzerGosti.nadjiImeGosta("anči"));
	}
	@Test
	void testNadjiPrezimeGosta() {
		main.zapisPodataka();
		assertEquals("Anić", menadzerGosti.nadjiPrezimeGosta("anči"));
	}
	@Test
	void testNadjiPolGosta() {
        main.zapisPodataka();
        assertEquals("ZENSKI", menadzerGosti.nadjiPolGosta("anči").toString());
	}
    @Test
	void testNadjiAdresuGosta() {
		main.zapisPodataka();
		assertEquals("Starih boraca 19, Sombor", menadzerGosti.nadjiAdresuGosta("anči"));
	}
    @Test
    void testNadjiTelefonGosta() {
		main.zapisPodataka();
		assertEquals(6666666667L, menadzerGosti.nadjiTelefonGosta("anči"));
    }
    @Test
    void testNadjiDatumRodjenjaGosta() {
        main.zapisPodataka();
        assertEquals("23.10.1977.", menadzerGosti.nadjiDatumRodjenjaGosta("anči"));
    }
    @Test
	void testNadjiLozinkuGosta() {
		main.zapisPodataka();
		// PROMENJENA JE LOZINKA U PRETHODNOM TESTU
		assertEquals("anči1234", menadzerGosti.nađiLozinkuGosta("anči"));
    }
}

package com.opencsv;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Testes gerados manualmente com apoio de LLM (ChatGPT)
 * Objetivo: cobrir casos de borda e comportamentos semânticos da classe CSVReader.
 *
 * Estes testes NÃO foram gerados por ferramentas SBST (EvoSuite),
 * servindo como base de comparação qualitativa e quantitativa.
 */
public class CSVReaderLLMTest {

    /**
     * Testa a leitura básica de uma única linha CSV simples.
     * Caso esperado: três campos corretamente separados por vírgula.
     */
    @Test
    public void testReadSimpleLine() throws IOException {
        String csv = "a,b,c";
        CSVReader reader = new CSVReader(new StringReader(csv));

        String[] result = reader.readNext();

        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("a", result[0]);
        assertEquals("b", result[1]);
        assertEquals("c", result[2]);
    }

    /**
     * Testa leitura de campos entre aspas contendo vírgulas internas.
     * Caso de borda clássico em CSV.
     */
    @Test
    public void testQuotedFieldWithComma() throws IOException {
        String csv = "\"a,b\",c";
        CSVReader reader = new CSVReader(new StringReader(csv));

        String[] result = reader.readNext();

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("a,b", result[0]);
        assertEquals("c", result[1]);
    }

    /**
     * Testa leitura de múltiplas linhas usando readAll().
     * Verifica se todas as linhas são corretamente transformadas em registros.
     */
    @Test
    public void testReadAllMultipleLines() throws IOException {
        String csv = "a,b\nc,d\ne,f";
        CSVReader reader = new CSVReader(new StringReader(csv));

        List<String[]> all = reader.readAll();

        assertEquals(3, all.size());
        assertEquals("a", all.get(0)[0]);
        assertEquals("d", all.get(1)[1]);
        assertEquals("f", all.get(2)[1]);
    }

    /**
     * Testa o comportamento de campos vazios.
     * CSVs podem conter campos sem conteúdo entre separadores.
     */
    @Test
    public void testEmptyFields() throws IOException {
        String csv = "a,,c";
        CSVReader reader = new CSVReader(new StringReader(csv));

        String[] result = reader.readNext();

        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("a", result[0]);
        assertEquals("", result[1]);
        assertEquals("c", result[2]);
    }

    /**
     * Testa a funcionalidade de pular linhas iniciais (skipLines).
     * Muito comum para ignorar cabeçalhos.
     */
    @Test
    public void testSkipLines() throws IOException {
        String csv = "header1,header2\na,b";
        CSVReader reader = new CSVReader(new StringReader(csv), ',', '"', 1);

        String[] result = reader.readNext();

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("a", result[0]);
        assertEquals("b", result[1]);
    }

    /**
     * Testa leitura de campo com quebra de linha dentro de aspas.
     * Este é um caso de borda importante que exige múltiplas leituras internas.
     */
    @Test
    public void testMultilineQuotedField() throws IOException {
        String csv = "\"a\nb\",c";
        CSVReader reader = new CSVReader(new StringReader(csv));

        String[] result = reader.readNext();

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("a\nb", result[0]);
        assertEquals("c", result[1]);
    }

    /**
     * Testa comportamento ao encontrar campo com aspas não terminadas.
     * O CSVReader deve lançar IOException.
     */
    @Test(expected = IOException.class)
    public void testUnterminatedQuotedFieldThrowsException() throws IOException {
        String csv = "\"a,b";
        CSVReader reader = new CSVReader(new StringReader(csv));

        reader.readNext();
    }

    /**
     * Testa se o contador de registros lidos é incrementado corretamente.
     * Verifica comportamento interno da classe.
     */
    @Test
    public void testRecordsReadCounter() throws IOException {
        String csv = "a,b\nc,d";
        CSVReader reader = new CSVReader(new StringReader(csv));

        reader.readNext();
        reader.readNext();

        assertEquals(2, reader.getRecordsRead());
    }

    /**
     * Testa se o contador de linhas lidas considera corretamente múltiplas linhas.
     */
    @Test
    public void testLinesReadCounter() throws IOException {
        String csv = "a,b\nc,d";
        CSVReader reader = new CSVReader(new StringReader(csv));

        reader.readNext();
        reader.readNext();

        assertTrue(reader.getLinesRead() >= 2);
    }
}

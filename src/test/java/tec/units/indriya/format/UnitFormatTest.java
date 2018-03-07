/*
 * Units of Measurement Reference Implementation
 * Copyright (c) 2005-2018, Jean-Marie Dautelle, Werner Keil, Otavio Santana.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *    and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of JSR-385, Indriya nor the names of their contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package tec.units.indriya.format;

import static org.junit.Assert.*;
import static tec.units.indriya.AbstractUnit.ONE;
import static tec.units.indriya.unit.MetricPrefix.KILO;
import static tec.units.indriya.unit.MetricPrefix.MEGA;
import static tec.units.indriya.unit.Units.*;

import java.io.IOException;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.format.ParserException;
import javax.measure.format.UnitFormat;
import javax.measure.quantity.Frequency;
import javax.measure.quantity.Length;
import javax.measure.quantity.Speed;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.junit.rules.ExpectedException;
import tec.units.indriya.format.LocalUnitFormat;
import tec.units.indriya.format.SimpleUnitFormat;
import tec.units.indriya.quantity.Quantities;
import tec.units.indriya.unit.Units;

/**
 * @author <a href="mailto:units@catmedia.us">Werner Keil</a>
 *
 */
public class UnitFormatTest {
  private Quantity<Length> sut;
  private UnitFormat format;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void init() {
    // sut =
    // DefaultQuantityFactoryService.getQuantityFactory(Length.class).create(10,
    // METRE);
    sut = Quantities.getQuantity(10, METRE);
    format = SimpleUnitFormat.getInstance();
  }

  @Test
  public void testFormat() {
    Unit<Frequency> hz = HERTZ;
    assertEquals("Hz", hz.toString());
  }

  @Test
  public void testFormat2() {
    Unit<Frequency> mhz = MEGA(HERTZ);
    assertEquals("MHz", mhz.toString());
  }

  @Test
  public void testFormat3() {
    Unit<Frequency> khz = KILO(HERTZ);
    assertEquals("kHz", khz.toString());
  }

  @Test
  public void testFormat4() {
    Unit<Speed> kph = Units.KILOMETRE_PER_HOUR;
    assertEquals("km/h", kph.toString());
  }

  @Test
  public void testFormatLocal() throws IOException {
    final UnitFormat format = LocalUnitFormat.getInstance();
    final Appendable appendable = new StringBuilder();
    format.format(METRE, appendable);
    assertEquals(METRE, sut.getUnit());
    assertEquals("m", appendable.toString());

    final Appendable a2 = new StringBuilder();
    @SuppressWarnings("unchecked")
    Unit<Speed> v = (Unit<Speed>) METRE.divide(SECOND);
    format.format(v, a2);
    assertEquals("m/s", a2.toString());
  }

  @Test
  public void testParseSimple1() {
  	Unit<?> u = format.parse("min");
    // assertEquals("min", u.getSymbol());
    assertEquals(MINUTE, u);
  }

  @Test
  public void testParseSimple2() {
    Unit<?> u = format.parse("m");
    assertNotNull(u);
    assertEquals("m", u.getSymbol());
    assertEquals(METRE, u);
  }

  @Test
  public void testParseSimple3() {
  	Unit<?> u = format.parse("kg");
  	assertEquals("kg", u.getSymbol());
  	assertEquals(KILOGRAM, u);
  }

  @Test
  public void testParseSimpleBlank() {
    Unit<?> u = format.parse("");
    assertEquals(ONE, u);
  }

  @Test
  public void testParseSimpleONE() {
  	Unit<?> u = format.parse("one");
  	assertEquals(ONE, u);
  }

  @Test
  public void testParseLocal() {
    final UnitFormat format = LocalUnitFormat.getInstance();
    expectedException.expect(UnsupportedOperationException.class);
    format.parse("min");
  }

  @Test
  public void testParseIrregularStringSimple() {
    final UnitFormat format = SimpleUnitFormat.getInstance();
    expectedException.expect(ParserException.class);
    format.parse("bl//^--1a");
  }
}

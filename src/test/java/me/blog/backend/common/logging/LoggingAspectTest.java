package me.blog.backend.common.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.boot.test.context.SpringBootTest;

import org.aspectj.lang.Signature;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
class LoggingAspectTest {

  @InjectMocks
  private LoggingAspect loggingAspect;
  @Mock
  private ProceedingJoinPoint joinPoint;
  @Mock
  private Signature signature;

  @BeforeEach
  public void setUp(){
    MockitoAnnotations.openMocks(this);
    when(joinPoint.getSignature()).thenReturn(signature);
  }

  @Test
  public void test_logAround_with_arguments() throws Throwable {
    // given
    when(joinPoint.getSignature().getDeclaringTypeName()).thenReturn("me.blog.backend.api.SampleObject");
    when(joinPoint.getSignature().getName()).thenReturn("sampleMethod");
    when(joinPoint.getArgs()).thenReturn(new Object[]{"arg1",123});
    // mocking the proceed() method
    when(joinPoint.proceed()).thenReturn("Success");

    // when
    Object result = loggingAspect.logAround(joinPoint);

    // then
    assertEquals("Success", result);
    verify(joinPoint, times(1)).proceed();
  }


  @Test
  public void test_logAround_with_exception() throws Throwable {
    // given
    when(joinPoint.getSignature().getDeclaringTypeName()).thenReturn("me.blog.backend.api.SampleController");
    when(joinPoint.getSignature().getName()).thenReturn("sampleMethod");
    when(joinPoint.getArgs()).thenReturn(new Object[]{"arg1", 123});
    // Mocking the proceed() method to throw an exception
    doThrow(new RuntimeException("Test Exception")).when(joinPoint).proceed();

    // when
    RuntimeException exception = assertThrows(RuntimeException.class, () -> loggingAspect.logAround(joinPoint));

    // then
    assertEquals("Test Exception", exception.getMessage());
    verify(joinPoint, times(1)).proceed();
  }


  @Test
  public void test_toJson_with_valid_object(){
    // given
    SampleObject sampleObject = new SampleObject("value1", 123);

    // when
    String jsonResult = loggingAspect.toJson(sampleObject);

    // then
    assertNotNull(jsonResult);
    assertTrue(jsonResult.contains("value1"));
    assertTrue(jsonResult.contains("123"));
  }

  @Test
  public void test_ToJson_with_invalid_object() {
    // Given
    Object invalidObject = new Object();

    // When
    String jsonResult = loggingAspect.toJson(invalidObject);

    // Then
    assertNotNull(jsonResult);
    assertTrue(jsonResult.contains("Error converting object to JSON"));
  }


  public class SampleObject {
    private String field1;
    private int field2;

    public SampleObject(String field1, int field2) {
      this.field1 = field1;
      this.field2 = field2;
    }

    public String getField1() {
      return field1;
    }

    public void setField1(String field1) {
      this.field1 = field1;
    }

    public int getField2() {
      return field2;
    }

    public void setField2(int field2) {
      this.field2 = field2;
    }
  }
}
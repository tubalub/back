package tubalubback.aspects;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Log4j2
public class LoggingAspect {

    @AfterThrowing(pointcut = "within(tubalubback.*)", throwing = "e")
    public void logException(JoinPoint jp, Exception e) throws Throwable {
        System.out.println("logException called");
        log.error("EXCEPTION THROWN FOR {} ON {}", jp.getTarget(), jp.getSignature());
        log.error("STACK TRACE", e);
    }

    @Before("within(tubalubback.*)")
    public void logMethodCalls(JoinPoint jp) {
        System.out.println("logMethodCalls called");
        log.info(jp.getTarget().toString());
        log.info(jp.getSignature().toString());
    }

}

package ua.com.javarush.jsquad.m3.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ua.com.javarush.jsquad.m3.model.Step;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ServletsTest {

    private static final String CONTEXT = "/michael";

    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Mock
    HttpSession session;
    @Mock
    RequestDispatcher dispatcher;

    private final Map<String, Object> attributes = new HashMap<>();

    @BeforeEach
    void setUp() {
        when(req.getContextPath()).thenReturn(CONTEXT);
        when(req.getSession()).thenReturn(session);
        when(req.getSession(false)).thenReturn(session);
        when(req.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        when(session.getAttribute(anyString())).thenAnswer(i -> attributes.get(i.<String>getArgument(0)));
        doAnswer(i -> attributes.put(i.getArgument(0), i.getArgument(1)))
                .when(session).setAttribute(anyString(), any());
        doAnswer(i -> attributes.remove(i.<String>getArgument(0)))
                .when(session).removeAttribute(anyString());
    }

    // ---------- StartServlet ----------

    @Test
    void startSavesNameStepAndFirstGame() throws Exception {
        when(req.getParameter("playerName")).thenReturn("  Michael ");

        new StartServlet().doPost(req, resp);

        assertEquals("Michael", attributes.get(SessionKeys.PLAYER_NAME));
        assertEquals("start", attributes.get(SessionKeys.CURRENT_STEP_ID));
        assertEquals(1, attributes.get(SessionKeys.GAMES_COUNT));
        verify(resp).sendRedirect(CONTEXT + "/game");
    }

    @Test
    void startWithBlankNameUsesDefaultName() throws Exception {
        when(req.getParameter("playerName")).thenReturn("   ");

        new StartServlet().doPost(req, resp);

        assertEquals("Мандрівник", attributes.get(SessionKeys.PLAYER_NAME));
    }

    @Test
    void startingTwiceIncreasesGamesCounter() throws Exception {
        when(req.getParameter("playerName")).thenReturn("Michael");
        StartServlet servlet = new StartServlet();

        servlet.doPost(req, resp);
        servlet.doPost(req, resp);

        assertEquals(2, attributes.get(SessionKeys.GAMES_COUNT));
    }

    // ---------- GameServlet ----------

    @Test
    void gameGetWithoutGameRedirectsToWelcomePage() throws Exception {
        new GameServlet().doGet(req, resp);

        verify(resp).sendRedirect(CONTEXT + "/");
        verify(dispatcher, never()).forward(any(), any());
    }

    @Test
    void gameGetShowsCurrentStep() throws Exception {
        playerIsOnStep("start");

        new GameServlet().doGet(req, resp);

        verify(req).setAttribute(eq("step"), argThat(step -> ((Step) step).id().equals("start")));
        verify(req).getRequestDispatcher("/WEB-INF/views/game.jsp");
        verify(dispatcher).forward(req, resp);
    }

    @Test
    void gamePostMovesToNextStep() throws Exception {
        playerIsOnStep("start");
        when(req.getParameter("answer")).thenReturn("0");

        new GameServlet().doPost(req, resp);

        assertEquals("restarted", attributes.get(SessionKeys.CURRENT_STEP_ID));
        verify(resp).sendRedirect(CONTEXT + "/game");
    }

    @Test
    void gamePostWithOutOfRangeAnswerReturns400() throws Exception {
        playerIsOnStep("start");
        when(req.getParameter("answer")).thenReturn("42");

        new GameServlet().doPost(req, resp);

        assertEquals("start", attributes.get(SessionKeys.CURRENT_STEP_ID));
        verify(resp).sendError(eq(HttpServletResponse.SC_BAD_REQUEST), anyString());
    }

    @Test
    void gamePostWithMissingAnswerReturns400() throws Exception {
        playerIsOnStep("start");
        when(req.getParameter("answer")).thenReturn(null);

        new GameServlet().doPost(req, resp);

        verify(resp).sendError(eq(HttpServletResponse.SC_BAD_REQUEST), anyString());
    }

    @Test
    void gamePostWithoutGameRedirectsToWelcomePage() throws Exception {
        when(req.getParameter("answer")).thenReturn("0");

        new GameServlet().doPost(req, resp);

        verify(resp).sendRedirect(CONTEXT + "/");
    }

    // ---------- RestartServlet ----------

    @Test
    void restartResetsStepAndCountsNewGame() throws Exception {
        playerIsOnStep("lose_loop");
        attributes.put(SessionKeys.GAMES_COUNT, 3);

        new RestartServlet().doPost(req, resp);

        assertEquals("start", attributes.get(SessionKeys.CURRENT_STEP_ID));
        assertEquals(4, attributes.get(SessionKeys.GAMES_COUNT));
        assertEquals("Michael", attributes.get(SessionKeys.PLAYER_NAME));
        verify(resp).sendRedirect(CONTEXT + "/game");
    }

    // ---------- FinishServlet ----------

    @Test
    void finishRemovesCurrentStepButKeepsNameAndCounter() throws Exception {
        playerIsOnStep("logs");
        attributes.put(SessionKeys.GAMES_COUNT, 2);

        new FinishServlet().doPost(req, resp);

        assertFalse(attributes.containsKey(SessionKeys.CURRENT_STEP_ID));
        assertEquals("Michael", attributes.get(SessionKeys.PLAYER_NAME));
        assertEquals(2, attributes.get(SessionKeys.GAMES_COUNT));
        verify(resp).sendRedirect(CONTEXT + "/");
    }

    @Test
    void finishWithoutSessionJustRedirects() throws Exception {
        when(req.getSession(false)).thenReturn(null);

        new FinishServlet().doPost(req, resp);

        verify(resp).sendRedirect(CONTEXT + "/");
    }
    @Test
    void finishMarksGameAsFinishedForTheDialog() throws Exception {
        playerIsOnStep("logs");

        new FinishServlet().doPost(req, resp);

        assertEquals(Boolean.TRUE, attributes.get(SessionKeys.GAME_FINISHED));
    }

    @Test
    void finishWithoutRunningGameShowsNoDialog() throws Exception {
        attributes.put(SessionKeys.PLAYER_NAME, "Michael");

        new FinishServlet().doPost(req, resp);

        assertFalse(attributes.containsKey(SessionKeys.GAME_FINISHED));
    }

    private void playerIsOnStep(String stepId) {
        attributes.put(SessionKeys.PLAYER_NAME, "Michael");
        attributes.put(SessionKeys.CURRENT_STEP_ID, stepId);
    }
}
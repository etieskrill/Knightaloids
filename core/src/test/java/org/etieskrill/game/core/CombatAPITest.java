package org.etieskrill.game.core;

import org.etieskrill.game.core.card.BlockAbilityCard;
import org.etieskrill.game.core.card.Card;
import org.etieskrill.game.core.card.SlashAbilityCard;
import org.etieskrill.game.core.entity.Goblin;
import org.etieskrill.game.core.entity.Knight;
import org.etieskrill.game.core.entity.Player;
import org.etieskrill.game.core.entity.Skeleton;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CombatAPITest {

    private static final String PLAYER_NAME = "hodekoboud";

    private static Card cardAttack;

    private CombatAPI combatUninitialised;
    private CombatAPI combat;

    @BeforeAll
    static void beforeAll() {
        cardAttack = new SlashAbilityCard();
    }

    @BeforeEach
    void beforeEach() {
        combatUninitialised = new CombatAPIImpl();

        combat = new CombatAPIImpl();
        combat.setPlayer(new Player(null, 4, 100, null, PLAYER_NAME));
        List<Card> initialCards = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            initialCards.add(new SlashAbilityCard());
            initialCards.add(new BlockAbilityCard());
        }
        combat.setDrawPile(initialCards);
        combat.addEntities(new Knight(), new Goblin(), new Goblin(), new Skeleton());
        combat.drawCardsToHand(0);
    }

    @Test
    void testUninitialised() {
        assertThrows(IllegalStateException.class, () -> combatUninitialised.drawCardsToHand(0));
    }

    @Test
    void testGetNoPlayer() {
        assertNull(combatUninitialised.getPlayer());
    }

    @Test
    void testGetPlayer() {
        assertNotNull(combat.getPlayer());
        assertEquals(PLAYER_NAME, combat.getPlayer().getDisplayName());
    }

    @Test
    void testSetDeck() {
        combat.setDrawPile(List.of(cardAttack, cardAttack));
        assertEquals(2, combat.getDrawPile().size());
    }

    @Test
    void testCardsDrawn() {
        assertEquals(4, combat.getHandCards().size());
    }

    @Test
    void testEntities() {
        assertEquals(4, combat.getEntities().size());
        assertEquals(1, combat.getAllies().size());
        assertEquals(3, combat.getEnemies().size());
    }

    @Test
    void testSetEntities() {
        combat.addEntities(new Knight(), new Goblin());

        assertEquals(6, combat.getEntities().size());
        assertEquals(2, combat.getAllies().size());
        assertEquals(4, combat.getEnemies().size());
    }

    @AfterEach
    void afterEach() {
        combatUninitialised = null;
        combat = null;
    }

    @AfterAll
    static void afterAll() {
        cardAttack = null;
    }


}

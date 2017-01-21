package unalcol.agents.examples.labyrinth.teseo;

import unalcol.agents.Agent;
import unalcol.agents.examples.labyrinth.Labyrinth;
import unalcol.agents.examples.labyrinth.LabyrinthDrawer;
import unalcol.agents.examples.labyrinth.LabyrinthPercept;
import unalcol.agents.simulate.util.SimpleLanguage;
import unalcol.types.collection.vector.Vector;

public class TeseoLabyrinth extends Labyrinth {

    public TeseoLabyrinth(Vector<Agent> _agents, int[][] _structure, SimpleLanguage _language) {
        super(_agents, _structure, _language);
    }

    public TeseoLabyrinth(Agent agent, int[][] _structure, SimpleLanguage _language) {
        super(agent, _structure, _language);
    }

    protected LabyrinthPercept getPercept(int x, int y) {
        return new TeseoPercept(structure[x][y], language);
    }

    public Labyrinth copy() {
        return new TeseoLabyrinth(agents, structure.clone(), language);
    }

    public boolean edit(int X, int Y) {
        boolean flag = super.edit(X, Y);
        if (!flag) {
            X -= LabyrinthDrawer.MARGIN;
            Y -= LabyrinthDrawer.MARGIN;
            int x = X / LabyrinthDrawer.CELL_SIZE;
            int y = Y / LabyrinthDrawer.CELL_SIZE;
            if (0 <= x && x < LabyrinthDrawer.DIMENSION && 0 <= y && y < LabyrinthDrawer.DIMENSION) {
                structure[x][y] ^= (1 << 4);
                flag = true;
            } else {
                flag = false;
            }
        }
        return flag;
    }

}

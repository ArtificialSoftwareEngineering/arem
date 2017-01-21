package unalcol.agents.examples.sudoku.naive;

import unalcol.agents.Action;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;
import unalcol.agents.examples.sudoku.SudokuPercept;
import unalcol.agents.search.classic.DepthFirstSearch;
import unalcol.agents.search.util.ConstantCost;
import unalcol.types.collection.vector.Vector;

/**
 * <p>Title: </p>
 * <p>
 * <p>Description: </p>
 * <p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>
 * <p>Company: Universidad Nacional de Colombia</p>
 *
 * @author Jonatan Gómez
 * @version 1.0
 */
public class NaiveSudokuAgentProgram implements AgentProgram {
    Vector<Action> cmd = new Vector<Action>();

    public NaiveSudokuAgentProgram() {
    }

    public void init() {
        cmd.clear();
    }

    public Action compute(Percept percept) {
        if (cmd.size() == 0) {
            NaiveSudokuBoardState initial_state = new NaiveSudokuBoardState((SudokuPercept) percept);
            int depth = initial_state.board.emptyPlaces();
            DepthFirstSearch search = new DepthFirstSearch(depth);
            cmd = search.apply(initial_state, new NaiveSudokuSearchSpace(),
                    new NaiveSudokuGoalTest(), new ConstantCost());
            if (cmd == null) {
                cmd = new Vector<Action>();
            }
        }
        if (cmd.size() > 0) {
            Action action = cmd.get(0);
            cmd.remove(0);
            return action;
        }
        return null;
    }
}

package moe.zenbu.app.commands.transitions;

import org.jrebirth.af.core.command.DefaultMultiBeanCommand;
import org.jrebirth.af.core.command.basic.showmodel.DetachModelCommand;
import org.jrebirth.af.core.command.basic.showmodel.DisplayModelWaveBean;
import org.jrebirth.af.core.command.basic.showmodel.ShowModelCommand;

public class ShowSlideRightModelCommand extends DefaultMultiBeanCommand<DisplayModelWaveBean>
{

    @Override
    protected void manageSubCommand()
    {
        //super.setSequential(true);
        System.out.println(super.isSequential());

        addCommandClass(ShowModelCommand.class);
        addCommandClass(SlideRightTransitionCommand.class);
        addCommandClass(DetachModelCommand.class);
    }

    @Override
    protected void initCommand()
    {
        //super.setSequential(true);
    }
}

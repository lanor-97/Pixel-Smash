package managers;

import java.util.ArrayList;
import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class ControllerManager {
    
    private Controller controller;

    private ArrayList<Boolean> buttonsValues;
    
    public ControllerManager(Controller.Type controllerType)
    {
        initialize();
        initController(controllerType, null);
    }
    
    public ControllerManager(Controller.Type controllerType_1, Controller.Type controllerType_2)
    {
        initialize();
        initController(controllerType_1, controllerType_2);
    }
    
    private void initialize()
    {
        this.controller = null;
        this.buttonsValues = new ArrayList<Boolean>();
    }
    
    private void initController(Controller.Type controllerType_1, Controller.Type controllerType_2)
    {
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        
        for(int i=0; i < controllers.length && controller == null; i++) {
            if(
               controllers[i].getType() == controllerType_1 ||
               controllers[i].getType() == controllerType_2
              )
            {
                controller = controllers[i];
                break;
            }
        }
    }
    
    public boolean isControllerConnected()
    {
        try {
            return controller.poll();
        } catch (Exception e) {
            return false;
        }
    }
    
    public Controller.Type getControllerType()
    {
        return controller.getType();
    }
    

    public String getControllerName()
    {
        return controller.getName();
    }
    
    public boolean pollController()
    {
        boolean isControllerValid;
        
        buttonsValues.clear();
        
        isControllerValid = controller.poll();
        if(!isControllerValid)
            return false;
        
        Component[] components = controller.getComponents();
        
        for(int i=0; i < components.length; i++) {
            Component component = components[i];
            
                if(component.getPollData() == 1.0f)
                    buttonsValues.add(Boolean.TRUE);
                else
                    buttonsValues.add(Boolean.FALSE);
        }
        
        return isControllerValid;
    }

    public boolean componentExists(Identifier identifier)
    {
        Component component = controller.getComponent(identifier);
        
        if(component != null)
            return true;
        else
            return false;
    }

    public float getComponentValue(Identifier identifier){
        return controller.getComponent(identifier).getPollData();
    }
    

    public int getNumberOfButtons()
    {
        return buttonsValues.size();
    }

    public ArrayList<Boolean> getButtonsValues()
    {
        return buttonsValues;
    }
    
    public boolean getButtonValue(int index)
    {
         return buttonsValues.get(index);
    }
    

    public float getXAxisValue()
    {
        Identifier identifier = Component.Identifier.Axis.X;
        return controller.getComponent(identifier).getPollData();
    }
 
    public int getXAxisPercentage()
    {
        float xAxisValue = this.getXAxisValue();
        int xAxisValuePercentage = (int)((2 - (1 - xAxisValue)) * 100) / 2;
        
        return xAxisValuePercentage;
    }
    
 
    public float getYAxisValue()
    {
        Identifier identifier = Component.Identifier.Axis.Y;
        return controller.getComponent(identifier).getPollData();
    }

 
    public int getYAxisPercentage()
    {
        float yAxisValue = this.getYAxisValue();
        int yAxisValuePercentage = (int)((2 - (1 - yAxisValue)) * 100) / 2;
        
        return yAxisValuePercentage;
    }
    
  
    public float getZRotationValue()
    {
        Identifier identifier = Component.Identifier.Axis.RZ;
        return controller.getComponent(identifier).getPollData();
    }
    
   
    public int getZRotationPercentage()
    {
        float zRotation = this.getZRotationValue();
        int zRotationValuePercentage = (int)((2 - (1 - zRotation)) * 100) / 2;
        
        return zRotationValuePercentage;
    }
    
  
    public float getZAxisValue()
    {
        Identifier identifier = Component.Identifier.Axis.Z;
        return controller.getComponent(identifier).getPollData();
    }
    
   
    public int getZAxisPercentage()
    {
        float zAxisValue = this.getZAxisValue();
        int zAxisValuePercentage = (int)((2 - (1 - zAxisValue)) * 100) / 2;
        
        return zAxisValuePercentage;
    }
    
  
    public float getXRotationValue()
    {
        Identifier identifier = Component.Identifier.Axis.RX;
        return controller.getComponent(identifier).getPollData();
    }
    
    public int getXRotationPercentage()
    {
        float xRotationValue = this.getXRotationValue();
        int xRotationValuePercentage = (int)((2 - (1 - xRotationValue)) * 100) / 2;
        
        return xRotationValuePercentage;
    }
    
   
    public float getYRotationValue()
    {
        Identifier identifier = Component.Identifier.Axis.RY;
        return controller.getComponent(identifier).getPollData();
    }
    
    
    public int getYRotationPercentage()
    {
        float yRotationValue = this.getYRotationValue();
        int yRotationValuePercentage = (int)((2 - (1 - yRotationValue)) * 100) / 2;
        
        return yRotationValuePercentage;
    }
    
    
    public float getHatSwitchPosition()
    {
        Identifier identifier = Component.Identifier.Axis.POV;
        return controller.getComponent(identifier).getPollData();
    }
    
   
    public float getX_LeftJoystick_Value()
    {
        return this.getXAxisValue();
    }
    
  
    public int getX_LeftJoystick_Percentage()
    {
        return this.getXAxisPercentage();
    }
    
   
    public float getY_LeftJoystick_Value()
    {
        return this.getYAxisValue();
    }
  
    public int getY_LeftJoystick_Percentage()
    {
        return this.getYAxisPercentage();
    }
    
    
    public float getX_RightJoystick_Value()
    {
        float xValueRightJoystick;
        
        // stick type controller
        if(this.controller.getType() == Controller.Type.STICK)
        {
            xValueRightJoystick = this.getZAxisValue();
        }
        // gamepad type controller
        else
        {
            xValueRightJoystick = this.getXRotationValue();
        }
        
        return xValueRightJoystick;
    }
    
  
    public int getX_RightJoystick_Percentage()
    {
        int xValueRightJoystickPercentage;
        
        // stick type controller
        if(this.controller.getType() == Controller.Type.STICK)
        {
            xValueRightJoystickPercentage = this.getZAxisPercentage();
        }
        // gamepad type controller
        else
        {
            xValueRightJoystickPercentage = this.getXRotationPercentage();
        }
        
        return xValueRightJoystickPercentage;
    }
    
    
    
    public float getY_RightJoystick_Value()
    {
        float yValueRightJoystick;
        
        // stick type controller
        if(this.controller.getType() == Controller.Type.STICK)
        {
            yValueRightJoystick = this.getZRotationValue();
        }
        // gamepad type controller
        else
        {
            yValueRightJoystick = this.getYRotationValue();
        }
        
        return yValueRightJoystick;
    }
   
    public int getY_RightJoystick_Percentage()
    {
        int yValueRightJoystickPercentage;
        
        // stick type controller
        if(this.controller.getType() == Controller.Type.STICK)
        {
            yValueRightJoystickPercentage = this.getZRotationPercentage();
        }
        // gamepad type controller
        else
        {
            yValueRightJoystickPercentage = this.getYRotationPercentage();
        }
        
        return yValueRightJoystickPercentage;
    }
}

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.Limelight;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class TeleopSwerveWithLimelight extends CommandBase {    

    private Swerve s_Swerve;    
    private Limelight l_Limelight;
    private DoubleSupplier translationSup;
    private DoubleSupplier strafeSup;
    private DoubleSupplier rotationSup;
    private BooleanSupplier robotCentricSup;

    private Boolean ToggleApriltagSnap = false;
    private Boolean ToggleButtonPressed = false;

    private XboxController driverController;

    public TeleopSwerveWithLimelight(Swerve s_Swerve, Limelight l_Limelight, DoubleSupplier translationSup, DoubleSupplier strafeSup, DoubleSupplier rotationSup, BooleanSupplier robotCentricSup, XboxController m_driverController) {

        this.s_Swerve = s_Swerve;
        this.l_Limelight = l_Limelight;
        this.driverController = m_driverController;
        addRequirements(s_Swerve);

        this.translationSup = translationSup;
        this.strafeSup = strafeSup;
        this.rotationSup = rotationSup;
        this.robotCentricSup = robotCentricSup;

    }

    @Override
    public void execute() {
        /* Get Values, Deadband*/
        double translationVal = MathUtil.applyDeadband(translationSup.getAsDouble(), Constants.stickDeadband);
        double strafeVal = MathUtil.applyDeadband(strafeSup.getAsDouble(), Constants.stickDeadband);
        double rotationVal = 0;

        if (ToggleApriltagSnap == false) {

            rotationVal = MathUtil.applyDeadband(rotationSup.getAsDouble(), Constants.stickDeadband);

        }  

        if (ToggleApriltagSnap == true && l_Limelight.HorizontalOffset() > 5) {

            rotationVal = .3;

        }  else if (l_Limelight.HorizontalOffset() < -5) {

            rotationVal = -.3;

        }  
 
        /* Drive */
        s_Swerve.drive(
            new Translation2d(translationVal, strafeVal).times(Constants.Swerve.maxSpeed), 
            rotationVal * Constants.Swerve.maxAngularVelocity, 
            !robotCentricSup.getAsBoolean(), 
            true
        );

    }

}
 
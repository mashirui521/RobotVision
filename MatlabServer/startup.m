function startup(sOption)

bGUI = true;
if nargin > 0 
    switch sOption
        case '-nogui'
            bGUI = false;
        otherwise
            error('invalid option "%s"', sOption);
    end
end

%% add jar
javaaddpath(fullfile(pwd, 'jars', 'supporter.jar'), '-end');

%% add scripts
addpath(fullfile(pwd, 'scripts', '01_JServerAPI'));
addpath(fullfile(pwd, 'scripts', '02_GUI'));

%% start gui
if bGUI
    gui;
end

end
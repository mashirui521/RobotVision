function startup()
% function startup(sOption)
% project starter
% 
% --- INPUT
% 
% --- OUTPUT
% 


%% add jar
javaaddpath(fullfile(pwd, 'jars', 'supporter.jar'), '-end');

%% add scripts
addpath(fullfile(pwd, 'scripts', '01_Supporter'));
addpath(fullfile(pwd, 'scripts', '02_GUI'));
addpath(fullfile(pwd, 'scripts', '03_API'));

end
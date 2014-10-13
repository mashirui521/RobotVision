display(sprintf('adding %s to javapath.', fullfile(pwd, 'jars', 'supporter.jar')));
javaaddpath(fullfile(pwd, 'jars', 'supporter.jar'), '-end');

display(sprintf('adding %s to path.', fullfile(pwd, 'scripts')));
addpath(fullfile(pwd, 'scripts'));

gui;
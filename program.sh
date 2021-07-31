#!/bin/bash

repl(){
  lein repl
}

main(){
  clojure -M:main
}

push(){
  ORIGIN=$(git remote get-url origin)
  rm -rf .git
  git init -b main
  git remote add origin $ORIGIN
  git config --local include.path ../.gitconfig
  git add .
  git commit -m "i am get-to-the-ship program"
  git push -f -u origin main
}

"$@"
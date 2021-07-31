#!/bin/bash

repl(){
  lein repl
}

main(){
  clojure -M:main
}

"$@"
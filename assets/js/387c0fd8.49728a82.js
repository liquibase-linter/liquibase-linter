"use strict";(self.webpackChunk=self.webpackChunk||[]).push([[970],{2146:(e,t,n)=>{n.r(t),n.d(t,{assets:()=>s,contentTitle:()=>a,default:()=>u,frontMatter:()=>o,metadata:()=>r,toc:()=>c});var i=n(4848),l=n(8453);const o={title:"Retrofitting"},a=void 0,r={id:"retrofitting",title:"Retrofitting",description:"Using Liquibase Linter in a brand new project is pretty straightforward, but more often than not you'll be retrofitting it to an existing project with a history of changes. It's likely that many of those changes would not pass the set of rules you are applying, but since changes are supposed to be immutable, fixing them retrospectively is not really an option.",source:"@site/docs/retrofitting.md",sourceDirName:".",slug:"/retrofitting",permalink:"/liquibase-linter/docs/retrofitting",draft:!1,unlisted:!1,tags:[],version:"current",frontMatter:{title:"Retrofitting"},sidebar:"docs",previous:{title:"Configure",permalink:"/liquibase-linter/docs/configure"},next:{title:"Using Rules",permalink:"/liquibase-linter/docs/rules/"}},s={},c=[{value:"<code>enable-after</code> at project level",id:"enable-after-at-project-level",level:2},{value:"<code>enableAfter</code> at rule level",id:"enableafter-at-rule-level",level:2}];function h(e){const t={code:"code",em:"em",h2:"h2",p:"p",pre:"pre",...(0,l.R)(),...e.components};return(0,i.jsxs)(i.Fragment,{children:[(0,i.jsx)(t.p,{children:"Using Liquibase Linter in a brand new project is pretty straightforward, but more often than not you'll be retrofitting it to an existing project with a history of changes. It's likely that many of those changes would not pass the set of rules you are applying, but since changes are supposed to be immutable, fixing them retrospectively is not really an option."}),"\n",(0,i.jsx)(t.p,{children:"Liquibase Linter provides some extra configuration options to help with this."}),"\n",(0,i.jsxs)(t.h2,{id:"enable-after-at-project-level",children:[(0,i.jsx)(t.code,{children:"enable-after"})," at project level"]}),"\n",(0,i.jsxs)(t.p,{children:["This config option allows you to specify a point in time (a change log file) ",(0,i.jsx)(t.em,{children:"after"})," which you want lint rules to be run. This would typically be the last change log before you add Liquibase Linter and turn on the rules."]}),"\n",(0,i.jsx)(t.p,{children:"Take this example configuration and change log:"}),"\n",(0,i.jsx)(t.pre,{children:(0,i.jsx)(t.code,{className:"language-json",children:'{\n    "enable-after": "src/main/resources/example-1.xml",\n    "rules": {}\n}\n'})}),"\n",(0,i.jsx)(t.pre,{children:(0,i.jsx)(t.code,{className:"language-xml",children:'\x3c!-- root change log file --\x3e\n<databaseChangeLog\n    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\n    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"\n    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.3.xsd">\n\n    <include relativeToChangelogFile="true" file="example-1.xml"/>\n    <include relativeToChangelogFile="true" file="example-2.xml"/>\n    <include relativeToChangelogFile="true" file="example-3.xml"/>\n\n</databaseChangeLog>\n'})}),"\n",(0,i.jsxs)(t.p,{children:["Since we've called out ",(0,i.jsx)(t.code,{children:"example-1.xml"})," as our ",(0,i.jsx)(t.code,{children:"enable-after"})," change log, the linter will start checking from ",(0,i.jsx)(t.code,{children:"example-2.xml"}),"."]}),"\n",(0,i.jsxs)(t.h2,{id:"enableafter-at-rule-level",children:[(0,i.jsx)(t.code,{children:"enableAfter"})," at rule level"]}),"\n",(0,i.jsxs)(t.p,{children:["Over time you'll probably want to add new rules to your project -- but again there may be historical changes that would fail if you just drop them in. For this, you can specify ",(0,i.jsx)(t.code,{children:"enableAfter"})," at rule level."]}),"\n",(0,i.jsx)(t.p,{children:"It works the same way as the root-level one; the value is a change log file name which marks the point in time after which you want the rule to be checked. For example:"}),"\n",(0,i.jsx)(t.pre,{children:(0,i.jsx)(t.code,{className:"language-json",children:'{\n    "rules": {\n        "has-context": {\n            "enableAfter": "last-changeset-before-contexts-became-mandatory.xml"        \n        }   \n    }\n}\n'})})]})}function u(e={}){const{wrapper:t}={...(0,l.R)(),...e.components};return t?(0,i.jsx)(t,{...e,children:(0,i.jsx)(h,{...e})}):h(e)}},8453:(e,t,n)=>{n.d(t,{R:()=>a,x:()=>r});var i=n(6540);const l={},o=i.createContext(l);function a(e){const t=i.useContext(o);return i.useMemo((function(){return"function"==typeof e?e(t):{...t,...e}}),[t,e])}function r(e){let t;return t=e.disableParentContext?"function"==typeof e.components?e.components(l):e.components||l:a(e.components),i.createElement(o.Provider,{value:t},e.children)}}}]);
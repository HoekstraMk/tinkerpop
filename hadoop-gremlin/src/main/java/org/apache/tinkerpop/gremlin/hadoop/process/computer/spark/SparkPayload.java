/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tinkerpop.gremlin.hadoop.process.computer.spark;

import org.apache.tinkerpop.gremlin.process.computer.MessageCombiner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface SparkPayload<M> {

    public default void addMessages(final List<M> otherMessages, final Optional<MessageCombiner<M>> messageCombinerOptional) {
        if (messageCombinerOptional.isPresent()) {
            final MessageCombiner<M> messageCombiner = messageCombinerOptional.get();
            final M combinedMessage = Stream.concat(this.getMessages().stream(), otherMessages.stream()).reduce(messageCombiner::combine).get();
            this.getMessages().clear();
            this.getMessages().add(combinedMessage);
        } else {
            this.getMessages().addAll(otherMessages);
        }
    }

    public List<M> getMessages();

    public boolean isVertex();

    public default SparkVertexPayload<M> asVertexPayload() {
        return (SparkVertexPayload<M>) this;
    }

}